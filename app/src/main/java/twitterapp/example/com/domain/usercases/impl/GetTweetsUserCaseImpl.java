package twitterapp.example.com.domain.usercases.impl;


import android.support.annotation.NonNull;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.List;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.usercases.GetTweetsUserCase;
import twitterapp.example.com.domain.usercases.base.AbstractUserCase;
import retrofit2.Call;

/**
 * User case implementation that get the user timeline.
 */
public class GetTweetsUserCaseImpl extends AbstractUserCase implements GetTweetsUserCase {

    private final StatusesService statusesService;
    private GetTweetsUserCase.Callback getTweetsCallback;

    public GetTweetsUserCaseImpl(Executor threadExecutor, MainThread mainThread, Callback callback) {
        super(threadExecutor, mainThread);
        this.getTweetsCallback = callback;

        // Getting the home timeline tweets
        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
        statusesService = twitterApiClient.getStatusesService();
    }


    @Override
    public void run() {
        // Retrieving 50 tweets as poc.
        Call<List<Tweet>> listCall = statusesService.homeTimeline(50, null, null, false,
                                                                  false, false, false);
        listCall.enqueue(getUserTweetsCallback());
    }

    @NonNull
    private com.twitter.sdk.android.core.Callback<List<Tweet>> getUserTweetsCallback() {
        return new com.twitter.sdk.android.core.Callback<List<Tweet>>() {
            @Override
            public void success(Result<List<Tweet>> result) {
                notifyTweetsRetrievedToMainThread(result.data);
            }

            @Override
            public void failure(TwitterException exception) {
                // We retrieve null when failing, that will be handled by the presenter.
                notifyTweetsRetrievedToMainThread(null);
            }
        };
    }

    private void notifyTweetsRetrievedToMainThread(final List<Tweet> tweetsRetrieved){
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                getTweetsCallback.onTweetsRetrieved(tweetsRetrieved);
            }
        });
    }
}
