package twitterapp.example.com.presentation.presenters;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Presenter interface that handles the actions that can happen in the timeline activity.
 */
public interface TimeLinePresenter {

    interface View {
        void onTweetsRetrieved(List<Tweet> tweets);
        void onFavoriteAdded(boolean added);
        void onFavoriteRemoved(boolean removed);
    }

    void showTweets();
    void addFavoriteTweet(Tweet tweet);
    void removeFavoriteTweet(Tweet tweet);
}
