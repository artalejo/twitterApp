package twitterapp.example.com.domain.usercases.impl;


import com.twitter.sdk.android.core.models.Tweet;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.RemoveFavoriteTweetUserCase;
import twitterapp.example.com.domain.usercases.base.AbstractUserCase;

/**
 * User case implementation that removes a favorite tweet.
 */
public class RemoveFavoriteTweetUserCaseImpl extends AbstractUserCase implements RemoveFavoriteTweetUserCase {

    private final Tweet tweetToAdd;
    private final TwitterRepository tweetRepository;
    private Callback removeFavoriteTweetCallback;

    public RemoveFavoriteTweetUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                           TwitterRepository tweetRepository, Tweet tweet,
                                           Callback callback) {
        super(threadExecutor, mainThread);
        this.removeFavoriteTweetCallback = callback;
        this.tweetToAdd = tweet;
        this.tweetRepository = tweetRepository;
    }


    @Override
    public void run() {

        final boolean removed = tweetRepository.deleteFavoriteTweet(tweetToAdd);
        // notify on the main thread that we have inserted this item
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                removeFavoriteTweetCallback.onFavoriteTweetRemoved(removed);
            }
        });

    }
}
