package twitterapp.example.com.domain.usercases;

import twitterapp.example.com.domain.usercases.base.UserCase;

/**
 * User case implementation that removes a favorite tweet.
 */
public interface RemoveFavoriteTweetUserCase extends UserCase {

    interface Callback {
        void onFavoriteTweetRemoved(boolean removed);
    }
}
