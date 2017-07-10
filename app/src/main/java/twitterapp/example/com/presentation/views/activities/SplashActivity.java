package twitterapp.example.com.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterSession;

import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.storage.TwitterRepositoryImpl;

/**
 * Splash Activity.
 */
public class SplashActivity extends AppCompatActivity{

    private TwitterRepository tweetRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tweetRepository = new TwitterRepositoryImpl(this);
        TwitterSession twitterSession = tweetRepository.loadTwitterSession();

        if (twitterSession == null) {
            Intent loginIntent = new Intent(SplashActivity.this, LoginActivity.class);
            startActivity(loginIntent);
        }
        else {
            // In case the twitter session exists, we set it as active session to allow the user
            // not to be logging in every time he uses the app.
            TwitterCore.getInstance().getSessionManager().setActiveSession(twitterSession);
            startActivity(new Intent(SplashActivity.this, TimeLineActivity.class));
        }
    }
}

