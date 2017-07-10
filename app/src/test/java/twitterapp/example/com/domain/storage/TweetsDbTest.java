package twitterapp.example.com.domain.storage;


import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.Shadows;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowApplication;

import java.util.ArrayList;

import twitterapp.example.com.storage.database.TweetsDb;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static twitterapp.example.com.storage.database.TweetsDb.FAVORITE_TWEETS_TABLE;
import static twitterapp.example.com.storage.database.TweetsDb.TWITTER_CONFIG_TABLE;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class TweetsDbTest {

    private TweetsDb tweetsDb;
    private Tweet sampleTweet;
    private String sampleTweetJson = "{\"coordinates\":{\"coordinates\":[-3.6884,40.41982],\"type\":\"Point\"},\"created_at\":\"Mon Jul 10 13:06:08 +0000 2017\",\"display_text_range\":[0,94],\"favorite_count\":1,\"favorited\":true,\"id\":884398331161706495,\"id_str\":\"884398331161706495\",\"in_reply_to_status_id\":0,\"in_reply_to_user_id\":0,\"lang\":\"es\",\"place\":{\"attributes\":{},\"bounding_box\":{\"coordinates\":[[[-3.8890049,40.3120713],[-3.5180102,40.3120713],[-3.5180102,40.6435181],[-3.8890049,40.6435181]]],\"type\":\"Polygon\"},\"country\":\"España\",\"country_code\":\"ES\",\"full_name\":\"Madrid, España\",\"id\":\"206c436ce43a43a3\",\"name\":\"Madrid\",\"place_type\":\"city\",\"url\":\"https://api.twitter.com/1.1/geo/id/206c436ce43a43a3.json\"},\"possibly_sensitive\":false,\"quoted_status_id\":0,\"retweet_count\":0,\"retweeted\":false,\"source\":\"\\u003ca href\\u003d\\\"http://instagram.com\\\" rel\\u003d\\\"nofollow\\\"\\u003eInstagram\\u003c/a\\u003e\",\"text\":\"Election Monday #NoBasketNopeace #labolanomiente @ Parque de El Retiro https://t.co/6oHoAkjl9l\",\"truncated\":false,\"user\":{\"contributors_enabled\":false,\"created_at\":\"Tue Sep 01 20:55:00 +0000 2009\",\"default_profile\":false,\"default_profile_image\":false,\"description\":\"Dios perdona, yo no. Talla de pie? Un 10 americano. Mi historia?\",\"entities\":{\"description\":{\"urls\":[]},\"url\":{\"urls\":[{\"display_url\":\"Canalplus.es/nba\",\"expanded_url\":\"http://Canalplus.es/nba\",\"url\":\"https://t.co/KxZouYh2IT\",\"indices\":[0,23]}]}},\"favourites_count\":3134,\"follow_request_sent\":false,\"followers_count\":46804,\"friends_count\":1001,\"geo_enabled\":true,\"id\":70782989,\"id_str\":\"70782989\",\"is_translator\":false,\"lang\":\"es\",\"listed_count\":834,\"location\":\"En tu Cancha\",\"name\":\"Jose Ajero\",\"profile_background_color\":\"131516\",\"profile_background_image_url\":\"http://pbs.twimg.com/profile_background_images/83376360/FotoTwit.jpg\",\"profile_background_image_url_https\":\"https://pbs.twimg.com/profile_background_images/83376360/FotoTwit.jpg\",\"profile_background_tile\":true,\"profile_banner_url\":\"https://pbs.twimg.com/profile_banners/70782989/1383050995\",\"profile_image_url\":\"http://pbs.twimg.com/profile_images/828953093077422081/fuhRRlZx_normal.jpg\",\"profile_image_url_https\":\"https://pbs.twimg.com/profile_images/828953093077422081/fuhRRlZx_normal.jpg\",\"profile_link_color\":\"009999\",\"profile_sidebar_border_color\":\"EEEEEE\",\"profile_sidebar_fill_color\":\"EFEFEF\",\"profile_text_color\":\"333333\",\"profile_use_background_image\":true,\"protected\":false,\"screen_name\":\"Jose_Ajero\",\"show_all_inline_media\":false,\"statuses_count\":26602,\"time_zone\":\"Madrid\",\"url\":\"https://t.co/KxZouYh2IT\",\"utc_offset\":7200,\"verified\":false},\"withheld_copyright\":false}";

    @Before
    public void setUp() throws Exception {
        ShadowApplication context = Shadows.shadowOf(RuntimeEnvironment.application);
        tweetsDb = TweetsDb.getInstance(context.getApplicationContext());
        Gson gson = new Gson();
        sampleTweet = gson.fromJson(this.sampleTweetJson, Tweet.class);
    }

    @After
    public void tearDown() throws Exception {
        cleanUpDatabase(new String[]{FAVORITE_TWEETS_TABLE, TWITTER_CONFIG_TABLE});
    }

    private void cleanUpDatabase(String[] tables) {
        SQLiteDatabase db = tweetsDb.getWritableDatabase();

        for (String table : tables) {
            db.execSQL("DROP TABLE IF EXISTS " + table);
        }
        tweetsDb.onCreate(db);
        tweetsDb.close();
    }

    @Test
    public void checkDatabaseName() {
        final String EXPECTED_DATABASE_NAME = "tweetsDb";
        assertEquals(EXPECTED_DATABASE_NAME, tweetsDb.getDatabaseName());
    }

    @Test
    public void insertFavoriteTweetReturnsFalseWhenNullTweet() {
        boolean result = tweetsDb.insertFavoriteTweet(null);
        assertFalse(result);
    }

    @Test
    public void insertFavoriteTweetReturnsTrueWhenTweetAdded() {
        boolean result = tweetsDb.insertFavoriteTweet(sampleTweet);
        assertTrue(result);
    }

    @Test
    public void deleteFavoriteTweetReturnsFalseWhenNullTweet() {
        boolean result = tweetsDb.deleteFavoriteTweet(null);
        assertFalse(result);
    }

    @Test
    public void deleteFavoriteTweetReturnsTrueWhenTweetRemoved() {
        // Adding tweet and later on removing it.
        tweetsDb.insertFavoriteTweet(sampleTweet);
        boolean result = tweetsDb.deleteFavoriteTweet(sampleTweet);
        assertTrue(result);
    }

    @Test
    public void deleteFavoriteTweetReturnsFalseWhenTweetToRemoveDoesNotExist() {
        boolean result = tweetsDb.deleteFavoriteTweet(sampleTweet);
        assertFalse(result);
    }

    @Test
    public void getFavoriteTweetsReturnTweetJustAdded() {
        final String expectedTweetID = "884398331161706495";
        boolean addedTweetResult = tweetsDb.insertFavoriteTweet(sampleTweet);

        ArrayList<Tweet> favoriteTweets = tweetsDb.getFavoriteTweets();

        assertTrue(addedTweetResult);
        assertEquals(expectedTweetID, favoriteTweets.get(0).idStr);
        assertTrue(favoriteTweets.get(0).favorited);
    }

    @Test
    public void getFavoriteTweetsReturnsEmptyListWhenNoTweets() {
        ArrayList<Tweet> favoriteTweets = tweetsDb.getFavoriteTweets();
        assertTrue(favoriteTweets.size() == 0);
    }

    @Test
    public void saveTweetSessionReturnsFalseWhenSessionNull() {
        boolean result = tweetsDb.saveTwitterSession(null);
        assertFalse(result);
    }

    @Test
    public void loadTwitterSessionReturnsJustAddedSession() {
        final long userID = 1;
        final String userName = "test user";
        final String token = "token";
        final String secret = "secret";
        // Adding twitter session
        TwitterAuthToken authToken = new TwitterAuthToken(token, secret);
        TwitterSession twitterSession = new TwitterSession(authToken, userID, userName);
        boolean addedSessionResult = tweetsDb.saveTwitterSession(twitterSession);

        TwitterSession twitterSessionRetrieved = tweetsDb.loadTwitterSession();
        assertTrue(addedSessionResult);
        assertEquals(twitterSessionRetrieved.getUserId(), userID);
        assertEquals(twitterSessionRetrieved.getUserName(), userName);
        assertEquals(twitterSessionRetrieved.getAuthToken().token, token);
        assertEquals(twitterSessionRetrieved.getAuthToken().secret, secret);
    }


}
