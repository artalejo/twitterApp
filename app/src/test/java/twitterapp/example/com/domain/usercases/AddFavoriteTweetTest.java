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
import twitterapp.example.com.domain.usercases.impl.AddFavoriteTweetUserCaseImpl;
import twitterapp.example.com.storage.database.TweetsDb;
import twitterapp.example.com.threading.TestMainThread;

import static org.mockito.Mockito.times;


public class AddFavoriteTweetTest {

    private MainThread mMainThread;
    @Mock private Executor mExecutor;
    @Mock private AddFavoriteTweetUserCase.Callback mockedCallback;
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
    public void notAddingFavoriteTweetWhenTweetIsNullTest() throws Exception {
        AddFavoriteTweetUserCaseImpl userCase =
                new AddFavoriteTweetUserCaseImpl(mExecutor, mMainThread, mockedTweetRepo,
                                                 null, mockedCallback);
        userCase.run();

        Mockito.verify(mockedCallback).onFavoriteTweetAdded(false);
        Mockito.verifyNoMoreInteractions(mockedCallback);
    }

    @Test
    public void addingFavoriteTweetWhenTweetNotNullTest() throws Exception {
        AddFavoriteTweetUserCaseImpl userCase =
                new AddFavoriteTweetUserCaseImpl(mExecutor, mMainThread, mockedTweetRepo,
                                                 mockedTweet, mockedCallback);
        userCase.run();

        Mockito.verify(mockedTweetRepo).insertFavoriteTweet(mockedTweet);
        Mockito.verifyNoMoreInteractions(mockedTweetRepo);
        Mockito.verify(mockedCallback, times(1)).onFavoriteTweetAdded(Mockito.anyBoolean());
    }
}
