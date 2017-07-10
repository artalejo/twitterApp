package twitterapp.example.com.presentation.presenters.impl;

import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.GetFavoriteTweetsUserCase;
import twitterapp.example.com.domain.usercases.RemoveFavoriteTweetUserCase;
import twitterapp.example.com.domain.usercases.impl.GetFavoriteTweetsUserCaseImpl;
import twitterapp.example.com.domain.usercases.impl.RemoveFavoriteTweetUserCaseImpl;
import twitterapp.example.com.presentation.presenters.AbstractPresenter;
import twitterapp.example.com.presentation.presenters.FavoriteTweetsPresenter;

/**
 * Presenter implementation that handles the favorite tweet actions.
 */
public class FavoriteTweetsPresenterImpl extends AbstractPresenter implements FavoriteTweetsPresenter,
        GetFavoriteTweetsUserCase.Callback, RemoveFavoriteTweetUserCase.Callback {


    private final TwitterRepository tweetRepository;
    private View mView;

    public FavoriteTweetsPresenterImpl(Executor executor, MainThread mainThread,
                                       View view, TwitterRepository tweetRepository) {
        super(executor, mainThread);
        this.mView = view;
        this.tweetRepository = tweetRepository;
    }

    @Override
    public void showFavoriteTweets() {
        GetFavoriteTweetsUserCase getFavoriteTweetsUserCase =
                new GetFavoriteTweetsUserCaseImpl(mExecutor, mMainThread, tweetRepository, this);
        getFavoriteTweetsUserCase.execute();
    }

    @Override
    public void onFavoriteTweetsRetrieved(List<Tweet> tweets) {
        mView.onFavoriteTweetsRetrieved(tweets);
    }

    @Override
    public void removeFavoriteTweet(Tweet tweet) {
        RemoveFavoriteTweetUserCase removeFavoriteTweetUserCase =
                new RemoveFavoriteTweetUserCaseImpl(mExecutor, mMainThread, tweetRepository,
                                                    tweet, this);
        removeFavoriteTweetUserCase.execute();
    }

    @Override
    public void onFavoriteTweetRemoved(boolean removed) {
        mView.onFavoriteRemoved(removed);
    }
}
