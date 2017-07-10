package twitterapp.example.com.presentation.views.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.User;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.TweetLinkClickListener;
import com.twitter.sdk.android.tweetui.TweetMediaClickListener;

import java.util.List;

import twitterapp.example.com.twitterapp.R;

/**
 * Adapter that handles the list of tweets.
 */
public class TweetsAdapter extends ArrayAdapter<Tweet> implements View.OnClickListener,
        TweetLinkClickListener, TweetMediaClickListener{


    private final Context context;
    private TimeLineActionsCallback timelineActionCallback;
    private List<Tweet> tweets;

    public TweetsAdapter(Context context, List<Tweet> tweets, TimeLineActionsCallback callback) {
        super(context, com.twitter.sdk.android.tweetui.R.layout.tw__tweet, tweets);
        this.context = context;
        this.timelineActionCallback = callback;
        this.tweets = tweets;
    }

    public void updateTweets(List<Tweet> tweets){
        this.tweets = tweets;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final Tweet tweet = tweets.get(position);
        BaseTweetView rowView = (BaseTweetView) convertView;

        if (rowView == null)
            rowView = new CompactTweetView(context, tweet, R.style.tw__TweetActionButtonBar);
        else
            rowView.setTweet(tweet);

        rowView.setTweetActionsEnabled(true);
        // To simplify all clicks in a tweet will show the user profile, apart from favorite action.
        rowView.setOnClickListener(this);
        rowView.setTweetLinkClickListener(this);
        rowView.setTweetMediaClickListener(this);
        rowView.setOnActionCallback(getFavoriteActionCallback());
        rowView.setTag(position);
        return rowView;
    }

    @Override
    public int getCount() {
        if (tweets != null)
            return tweets.size();

        return 0;
    }

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (tweets != null && position < tweets.size())
            timelineActionCallback.profileClicked(tweets.get(position).user);
    }

    @Override
    public void onLinkClick(Tweet tweet, String url) {
        timelineActionCallback.profileClicked(tweet.user);
    }

    @Override
    public void onMediaEntityClick(Tweet tweet, MediaEntity entity) {
        timelineActionCallback.profileClicked(tweet.user);
    }


    public Callback<Tweet> getFavoriteActionCallback() {
        return new Callback<Tweet>() {
            @Override
            public void success(Result<Tweet> result) {
                Tweet tweet = result.data;
                if (tweet.favorited)
                    timelineActionCallback.addFavoriteTweet(tweet);
                else
                    timelineActionCallback.removeFavoriteTweet(tweet);
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(context, R.string.tweet_favorites_error, Toast.LENGTH_SHORT).show();
            }
        };
    }

    public interface TimeLineActionsCallback {
        void profileClicked(User userClicked);
        void addFavoriteTweet(Tweet tweet);
        void removeFavoriteTweet(Tweet tweet);
    }
}
