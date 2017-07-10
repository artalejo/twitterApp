package twitterapp.example.com.domain.usercases;

import android.content.Context;

import com.twitter.sdk.android.core.models.Tweet;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;
import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.domain.usercases.impl.RemoveFavoriteTweetUserCaseImpl;
import twitterapp.example.com.threading.TestMainThread;

import static org.mockito.Mockito.times;


public class RemoveFavoriteTweetTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private RemoveFavoriteTweetUserCase.Callback mockedCallback;
    @Mock private Tweet mockedTweet;
    @Mock private Context mockedContext;
    @Mock private TwitterRepository mockedTweetRepo;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        mMainThread = new TestMainThread();
    }

    @Test
    public void notRemovingFavoriteTweetWhenTweetIsNullTest() throws Exception {
        RemoveFavoriteTweetUserCaseImpl userCase =
                new RemoveFavoriteTweetUserCaseImpl(mExecutor, mMainThread, mockedTweetRepo,
                                                    null, mockedCallback);
        userCase.run();

        Mockito.verify(mockedCallback).onFavoriteTweetRemoved(false);
        Mockito.verifyNoMoreInteractions(mockedCallback);
    }

    @Test
    public void removingFavoriteTweetWhenTweetNotNullTest() throws Exception {
        RemoveFavoriteTweetUserCaseImpl userCase =
                new RemoveFavoriteTweetUserCaseImpl(mExecutor, mMainThread, mockedTweetRepo,
                        mockedTweet, mockedCallback);
        userCase.run();

        Mockito.verify(mockedTweetRepo).deleteFavoriteTweet(mockedTweet);
        Mockito.verifyNoMoreInteractions(mockedTweetRepo);
        Mockito.verify(mockedCallback, times(1)).onFavoriteTweetRemoved(Mockito.anyBoolean());
    }
}
