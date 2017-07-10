package twitterapp.example.com.domain.usercases.impl;


import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.GetFavoriteTweetsUserCase;
import twitterapp.example.com.domain.usercases.base.AbstractUserCase;

/**
 * User case implementation that get the favorite tweets.
 */
public class GetFavoriteTweetsUserCaseImpl extends AbstractUserCase implements GetFavoriteTweetsUserCase {

    private TwitterRepository tweetRepository;
    private Callback getFavoriteTweetsCallback;

    public GetFavoriteTweetsUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                         TwitterRepository tweetRepository, Callback callback) {
        super(threadExecutor, mainThread);
        this.getFavoriteTweetsCallback = callback;
        this.tweetRepository = tweetRepository;
    }


    @Override
    public void run() {
        final List<Tweet> favoriteTweets =  tweetRepository.getFavoriteTweets();

        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                getFavoriteTweetsCallback.onFavoriteTweetsRetrieved(favoriteTweets);
            }
        });
    }
}
