package twitterapp.example.com.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitterapp.example.com.domain.executor.impl.ThreadExecutor;
import twitterapp.example.com.presentation.models.ProfileSummary;
import twitterapp.example.com.presentation.presenters.FavoriteTweetsPresenter;
import twitterapp.example.com.presentation.presenters.impl.FavoriteTweetsPresenterImpl;
import twitterapp.example.com.presentation.views.adapters.TweetsAdapter;
import twitterapp.example.com.storage.TwitterRepositoryImpl;
import twitterapp.example.com.threading.MainThreadImpl;
import twitterapp.example.com.twitterapp.R;

import static twitterapp.example.com.Constants.PROFILE_CLICKED;

/**
 * Activity that displays the favorite tweets.
 */
public class FavoriteTweetsActivity extends AppCompatActivity implements FavoriteTweetsPresenter.View, TweetsAdapter.TimeLineActionsCallback {

    @BindView(R.id.favorite_tweets_toolbar) Toolbar favTweetsToolbar;
    @BindView(R.id.favorite_tweets_list_view) ListView favTweetsListView;
    @BindView(R.id.empty_favorite_tweets) LinearLayout emptyLinear;

    private List<Tweet> favoriteTweets;
    private TweetsAdapter tweetsAdapter;
    private FavoriteTweetsPresenterImpl favoriteTweetsPresenter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.favorite_tweets_activity);
        ButterKnife.bind(this);
        init();

        favoriteTweetsPresenter = new FavoriteTweetsPresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this, new TwitterRepositoryImpl(this));
    }

    private void init() {
        favoriteTweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsAdapter(this, favoriteTweets, this);

        favTweetsListView.setAdapter(tweetsAdapter);
        favTweetsListView.setEmptyView(emptyLinear);

        if (favTweetsToolbar != null) {
            favTweetsToolbar.setTitle(R.string.favorite_tweets_activity_toolbar_title);
            setSupportActionBar(favTweetsToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        favoriteTweetsPresenter.showFavoriteTweets();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onFavoriteTweetsRetrieved(List<Tweet> tweets) {
        favoriteTweets = tweets;
        if (tweetsAdapter != null)
            tweetsAdapter.updateTweets(favoriteTweets);
    }

    @Override
    public void onFavoriteRemoved(boolean removed) {
        // Reloading the new favorite tweets if one has been removed
        if (removed)
            favoriteTweetsPresenter.showFavoriteTweets();

        int msg = removed ? R.string.tweet_removed_from_favorites: R.string.tweet_removed_from_favorites_error;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void profileClicked(User userClicked) {
        Intent showProfileIntent = new Intent(FavoriteTweetsActivity.this, ProfileActivity.class);
        ProfileSummary profileSummary = new ProfileSummary(userClicked);
        showProfileIntent.putExtra(PROFILE_CLICKED, profileSummary);
        startActivity(showProfileIntent);
    }

    @Override
    public void removeFavoriteTweet(Tweet tweet) {
        favoriteTweetsPresenter.removeFavoriteTweet(tweet);
    }

    @Override
    public void addFavoriteTweet(Tweet tweet) {
        // From the favorite view the user cannot add a favorite tweet.
    }
}
