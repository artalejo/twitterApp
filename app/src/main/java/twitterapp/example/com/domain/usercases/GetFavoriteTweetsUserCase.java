package twitterapp.example.com.domain.usercases;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.usercases.base.UserCase;

/**
 * User case interface that get favorite tweets.
 */
public interface GetFavoriteTweetsUserCase extends UserCase {

    interface Callback {
        void onFavoriteTweetsRetrieved(List<Tweet> tweets);
    }
}
