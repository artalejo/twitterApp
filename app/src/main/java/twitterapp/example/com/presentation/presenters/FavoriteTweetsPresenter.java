package twitterapp.example.com.presentation.presenters;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Presenter interface that handles the favorite tweet actions.
 */
public interface FavoriteTweetsPresenter {

    interface View{

        void onFavoriteTweetsRetrieved(List<Tweet> tweets);
        void onFavoriteRemoved(boolean removed);
    }

    void showFavoriteTweets();
    void removeFavoriteTweet(Tweet tweet);
}
