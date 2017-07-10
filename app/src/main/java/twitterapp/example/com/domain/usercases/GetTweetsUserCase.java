package twitterapp.example.com.domain.usercases;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.usercases.base.UserCase;

/**
 * User case interface that get the user timeline.
 */
public interface GetTweetsUserCase extends UserCase {

    interface Callback {
        void onTweetsRetrieved(List<Tweet> tweets);
    }
}
