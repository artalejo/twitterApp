package twitterapp.example.com.domain.repository;

import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.List;

/**
 * Interface that abstracts the way of retrieving the tweet information.
 */
public interface TwitterRepository {

    TwitterSession loadTwitterSession();

    boolean saveTwitterSession(TwitterSession twitterSession);

    boolean insertFavoriteTweet(Tweet tweet);

    boolean deleteFavoriteTweet(Tweet tweet);

    List<Tweet> getFavoriteTweets();

}
