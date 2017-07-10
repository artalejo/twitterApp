package twitterapp.example.com.presentation.presenters.impl;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.AddFavoriteTweetUserCase;
import twitterapp.example.com.domain.usercases.GetTweetsUserCase;
import twitterapp.example.com.domain.usercases.RemoveFavoriteTweetUserCase;
import twitterapp.example.com.domain.usercases.impl.AddFavoriteTweetUserCaseImpl;
import twitterapp.example.com.domain.usercases.impl.GetTweetsUserCaseImpl;
import twitterapp.example.com.domain.usercases.impl.RemoveFavoriteTweetUserCaseImpl;
import twitterapp.example.com.presentation.presenters.AbstractPresenter;
import twitterapp.example.com.presentation.presenters.TimeLinePresenter;

/**
 * Presenter implementation that handles the actions that can happen in the timeline activity.
 */
public class TimeLinePresenterImpl extends AbstractPresenter implements TimeLinePresenter,
        GetTweetsUserCaseImpl.Callback, AddFavoriteTweetUserCase.Callback, RemoveFavoriteTweetUserCase.Callback {


    private TwitterRepository tweetRepository;
    private TimeLinePresenter.View mView;

    public TimeLinePresenterImpl(Executor executor, MainThread mainThread,
                                 View view, TwitterRepository tweetRepository) {

        super(executor, mainThread);
        this.mView = view;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public void showTweets() {
        GetTweetsUserCase getTweetsUserCase = new GetTweetsUserCaseImpl(mExecutor, mMainThread, this);
        getTweetsUserCase.execute();
    }

    @Override
    public void addFavoriteTweet(Tweet tweet) {
        AddFavoriteTweetUserCase addFavoriteTweetUserCase =
                new AddFavoriteTweetUserCaseImpl(mExecutor, mMainThread, tweetRepository, tweet, this);
        addFavoriteTweetUserCase.execute();
    }

    @Override
    public void removeFavoriteTweet(Tweet tweet) {
        RemoveFavoriteTweetUserCase removeFavoriteTweetUserCase =
                new RemoveFavoriteTweetUserCaseImpl(mExecutor, mMainThread, tweetRepository, tweet, this);
        removeFavoriteTweetUserCase.execute();
    }

    @Override
    public void onTweetsRetrieved(List<Tweet> tweets) {
        mView.onTweetsRetrieved(tweets);
    }

    @Override
    public void onFavoriteTweetAdded(boolean added) {
        mView.onFavoriteAdded(added);
    }

    @Override
    public void onFavoriteTweetRemoved(boolean removed) {
        mView.onFavoriteRemoved(removed);
    }
}
