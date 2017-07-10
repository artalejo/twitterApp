package twitterapp.example.com.presentation.presenters;

import twitterapp.example.com.domain.executor.Executor;
import twitterapp.example.com.domain.executor.MainThread;

public abstract class AbstractPresenter {

    protected Executor mExecutor;
    protected MainThread mMainThread;

    public AbstractPresenter(Executor executor, MainThread mainThread) {
        mExecutor = executor;
        mMainThread = mainThread;
    }
}
