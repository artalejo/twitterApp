package twitterapp.example.com.storage;

import android.content.Context;

import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

import twitterapp.example.com.domain.repository.TwitterRepository;
import twitterapp.example.com.storage.database.TweetsDb;

/**
 * Repository implementation that abstracts the way of retrieving the tweet information.
 */
public class TwitterRepositoryImpl implements TwitterRepository {

    private final TweetsDb db;

    public TwitterRepositoryImpl(Context context) {
        db = TweetsDb.getInstance(context);
    }

    @Override
    public TwitterSession loadTwitterSession() {
        return db.loadTwitterSession();
    }

    @Override
    public boolean saveTwitterSession(TwitterSession twitterSession) {
        return db.saveTwitterSession(twitterSession);
    }

    @Override
    public boolean insertFavoriteTweet(Tweet tweet) {
        return db.insertFavoriteTweet(tweet);
    }

    @Override
    public boolean deleteFavoriteTweet(Tweet tweet) {
        return db.deleteFavoriteTweet(tweet);
    }

    @Override
    public List<Tweet> getFavoriteTweets() {
        return db.getFavoriteTweets();
    }
}
