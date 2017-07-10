package twitterapp.example.com.domain.usercases.impl;


import com.twitter.sdk.android.core.models.Tweet;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.AddFavoriteTweetUserCase;
import twitterapp.example.com.domain.usercases.base.AbstractUserCase;

/**
 * User case that adds a favorite tweet.
 */
public class AddFavoriteTweetUserCaseImpl extends AbstractUserCase implements AddFavoriteTweetUserCase {

    private final Tweet tweetToAdd;
    private TwitterRepository tweetRepository;
    private Callback addFavoriteTweetCallback;

    public AddFavoriteTweetUserCaseImpl(Executor threadExecutor, MainThread mainThread,
                                        TwitterRepository tweetRepository, Tweet tweet,
                                        Callback callback) {

        super(threadExecutor, mainThread);
        this.addFavoriteTweetCallback = callback;
        this.tweetToAdd = tweet;
        this.tweetRepository = tweetRepository;
    }


    @Override
    public void run() {

        final boolean added = tweetRepository.insertFavoriteTweet(tweetToAdd);
        // notify on the main thread that we have inserted this favorite tweet.
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                addFavoriteTweetCallback.onFavoriteTweetAdded(added);
            }
        });

    }
}
