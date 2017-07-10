package twitterapp.example.com.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitterapp.example.com.twitterapp.R;
import twitterapp.example.com.domain.executor.impl.ThreadExecutor;
import twitterapp.example.com.presentation.models.ProfileSummary;
import twitterapp.example.com.presentation.presenters.TimeLinePresenter;
import twitterapp.example.com.presentation.presenters.impl.TimeLinePresenterImpl;
import twitterapp.example.com.presentation.views.adapters.TweetsAdapter;
import twitterapp.example.com.storage.TwitterRepositoryImpl;
import twitterapp.example.com.threading.MainThreadImpl;

import static twitterapp.example.com.Constants.PROFILE_CLICKED;

/**
 * Activity that user timeline.
 */
public class TimeLineActivity extends AppCompatActivity implements TimeLinePresenter.View, TweetsAdapter.TimeLineActionsCallback {

    @BindView(R.id.tweets_toolbar) Toolbar tweetsToolbar;
    @BindView(R.id.tweets_list_view) ListView tweetsListView;
    @BindView(R.id.timeline_progress_linear) LinearLayout timelineProgressBar;

    private TweetsAdapter tweetsAdapter;
    private TimeLinePresenterImpl tweetsPresenter;
    private List<Tweet> timelineTweets;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_activity);
        ButterKnife.bind(this);
        init();

        tweetsPresenter = new TimeLinePresenterImpl(
                ThreadExecutor.getInstance(),
                MainThreadImpl.getInstance(),
                this, new TwitterRepositoryImpl(this));
    }

    private void init() {
        timelineTweets = new ArrayList<Tweet>();
        tweetsAdapter = new TweetsAdapter(this, timelineTweets, this);
        tweetsListView.setAdapter(tweetsAdapter);

        if (tweetsToolbar != null) {
            tweetsToolbar.setTitle(R.string.timeline_activity_toolbar_title);
            setSupportActionBar(tweetsToolbar);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        fetchTweets();
    }

    private void fetchTweets() {
        tweetsPresenter.showTweets();
        timelineProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.timeline_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.refresh_tweets:
                fetchTweets();
                return true;
            case R.id.favorite_tweets:
                startFavoriteTweetsActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // Removing all activities when moving to the back
        ActivityCompat.finishAffinity(this);
        moveTaskToBack(true);
    }

    private void startFavoriteTweetsActivity() {
        Intent favoriteTweetsIntent = new Intent(TimeLineActivity.this, FavoriteTweetsActivity.class);
        startActivity(favoriteTweetsIntent);
    }

    @Override
    public void profileClicked(User userClicked) {
        Intent showProfileIntent = new Intent(TimeLineActivity.this, ProfileActivity.class);
        ProfileSummary profileSummary = new ProfileSummary(userClicked);
        showProfileIntent.putExtra(PROFILE_CLICKED , profileSummary);
        startActivity(showProfileIntent);
    }

    @Override
    public void addFavoriteTweet(Tweet tweet) {
        tweetsPresenter.addFavoriteTweet(tweet);
    }

    @Override
    public void removeFavoriteTweet(Tweet tweet) {
        tweetsPresenter.removeFavoriteTweet(tweet);
    }

    @Override
    public void onTweetsRetrieved(List<Tweet> tweets) {
        timelineTweets = tweets;
        if (tweetsAdapter != null)
            tweetsAdapter.updateTweets(timelineTweets);
        timelineProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onFavoriteAdded(boolean added) {
        int msg = added ? R.string.tweet_added_to_favorites : R.string.tweet_added_to_favorites_error;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFavoriteRemoved(boolean removed) {
        int msg = removed ? R.string.tweet_removed_from_favorites: R.string.tweet_removed_from_favorites_error;
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}
