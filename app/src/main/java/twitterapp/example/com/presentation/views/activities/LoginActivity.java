package twitterapp.example.com.presentation.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitterapp.example.com.storage.TwitterRepositoryImpl;
import twitterapp.example.com.twitterapp.R;

/**
 * Activity that handles login.
 */
public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.twitter_login_button) TwitterLoginButton twitterLoginButton;
    private TwitterRepositoryImpl twitterRepository;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_main);
        ButterKnife.bind(this);
        twitterRepository = new TwitterRepositoryImpl(this);

        twitterLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                twitterRepository.saveTwitterSession(result.data);
                startActivity(new Intent(LoginActivity.this, TimeLineActivity.class));
            }

            @Override
            public void failure(TwitterException exception) {
                Toast.makeText(LoginActivity.this, R.string.log_in_error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Pass the activity result to the login button.
        twitterLoginButton.onActivityResult(requestCode, resultCode, data);
    }
}
