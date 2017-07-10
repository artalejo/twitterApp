package twitterapp.example.com.domain.usercases;

import twitterapp.example.com.domain.usercases.base.UserCase;

/**
 * User case interface that adds a favorite tweet.
 */
public interface AddFavoriteTweetUserCase extends UserCase {

    interface Callback {
        void onFavoriteTweetAdded(boolean added);
    }
}
