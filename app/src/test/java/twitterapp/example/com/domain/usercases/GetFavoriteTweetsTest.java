package twitterapp.example.com.domain.usercases;

import android.content.Context;

import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.impl.AddFavoriteTweetUserCaseImpl;
import twitterapp.example.com.domain.usercases.impl.GetFavoriteTweetsUserCaseImpl;
import twitterapp.example.com.storage.database.TweetsDb;
import twitterapp.example.com.threading.TestMainThread;


public class GetFavoriteTweetsTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private AddFavoriteTweetUserCase.Callback mockedAddTweetCallback;
    @Mock private GetFavoriteTweetsUserCase.Callback mockedGetTweetsCallback;
    @Mock private Tweet mockedTweet;
    @Mock private Context mockedContext;
    @Mock private TwitterRepository mockedTweetRepo;
    @Mock private TweetsDb mockedDb;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void getFavoritesTweetsTest() throws Exception {
        GetFavoriteTweetsUserCaseImpl userCase =
                new GetFavoriteTweetsUserCaseImpl(mExecutor, mMainThread,
                                                  mockedTweetRepo, mockedGetTweetsCallback);
        userCase.run();

        Mockito.verify(mockedGetTweetsCallback).onFavoriteTweetsRetrieved(Matchers.anyListOf(Tweet.class));
        Mockito.verifyNoMoreInteractions(mockedAddTweetCallback);
    }

    @Test
    public void getFavoritesTweetsTestJustAdded() throws Exception {
        // Adding a tweet
        AddFavoriteTweetUserCaseImpl addTweetUserCase =
                new AddFavoriteTweetUserCaseImpl(mExecutor, mMainThread, mockedTweetRepo,
                                                 mockedTweet, mockedAddTweetCallback);
        addTweetUserCase.run();

        GetFavoriteTweetsUserCaseImpl userCase =
                new GetFavoriteTweetsUserCaseImpl(mExecutor, mMainThread,
                                                  mockedTweetRepo, mockedGetTweetsCallback);
        userCase.run();

        Mockito.verify(mockedGetTweetsCallback).onFavoriteTweetsRetrieved(Matchers.anyListOf(Tweet.class));
        Mockito.verifyNoMoreInteractions(mockedAddTweetCallback);
    }
}
