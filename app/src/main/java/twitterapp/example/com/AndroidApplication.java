package twitterapp.example.com;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.twitter.sdk.android.core.Twitter;

/**
 * Android app where twitter sdk and stetho are initialized.
 */
public class AndroidApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Twitter.initialize(this);
        Stetho.initializeWithDefaults(this);
    }

}
