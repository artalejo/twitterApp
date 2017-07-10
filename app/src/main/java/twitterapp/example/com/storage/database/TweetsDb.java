package twitterapp.example.com.storage.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.models.Tweet;

import java.util.ArrayList;

/**
 * Sqlite Database Helper that handles the offline tweets as well as the twitter session.
 */
public class TweetsDb extends SQLiteOpenHelper {

    //Singleton
    private static final String DATABASE_NAME = "tweetsDb";
    private static final int DATABASE_VERSION = 1;

    public static final String FAVORITE_TWEETS_TABLE = "favorite_tweets";
    public static final String TWEET_ID = "tweet_id";
    public static final String TWEET_JSON = "tweet_json";

    public static final String TWITTER_CONFIG_TABLE = "twitter_config";
    public static final String TWITTER_SESSION = "twitter_session";

    private static TweetsDb dbInstance = null;
    private final Gson gson;
    private Context context;

    public static synchronized TweetsDb getInstance(Context context) {
        if (dbInstance == null) {
            dbInstance = new TweetsDb(context);
        }
        return dbInstance;
    }

    private TweetsDb(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        this.gson = new Gson();
    }

    private static String CREATE_FAVORITE_TWEETS_TABLE = "CREATE TABLE favorite_tweets (" +
            " tweet_id INT," +
            " tweet_json TEXT)";

    private static String CREATE_TWITTER_CONFIG_TABLE = "CREATE TABLE twitter_config (" +
            " twitter_session TEXT)";


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_FAVORITE_TWEETS_TABLE);
        db.execSQL(CREATE_TWITTER_CONFIG_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // DB FUNCTIONS

    public boolean insertFavoriteTweet(Tweet tweet) {
        if (tweet == null)
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tweetValue = new ContentValues();
        tweetValue.put(TWEET_ID, tweet.getId());
        tweetValue.put(TWEET_JSON, gson.toJson(tweet));
        db.insertWithOnConflict(FAVORITE_TWEETS_TABLE, null, tweetValue,
                                SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean deleteFavoriteTweet(Tweet tweet) {
        if (tweet == null)
            return false;

        SQLiteDatabase db = this.getWritableDatabase();
        int delete = db.delete(FAVORITE_TWEETS_TABLE, TWEET_ID + " = ?",
                new String[]{String.valueOf(tweet.getId())});

        return delete == 1;
    }

    public ArrayList<Tweet> getFavoriteTweets(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<Tweet> favoriteTweets = new ArrayList<>();
        String getTweetsQuery = "SELECT tweet_json FROM favorite_tweets";
        Tweet tweet;
        Cursor cursor = db.rawQuery(getTweetsQuery, null);

        try {
            while (cursor.moveToNext()) {
                String tweetJson = cursor.getString(0);
                tweet = gson.fromJson(tweetJson, Tweet.class);
                favoriteTweets.add(tweet);
            }
        } finally {
            cursor.close();
        }
        return favoriteTweets;
    }

    // Twitter Session Configuration

    public boolean saveTwitterSession(TwitterSession session) {

        if (session == null)
            return false;

        String twitterSessionJson = gson.toJson(session);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues tweetSessionValue = new ContentValues();
        tweetSessionValue.put(TWITTER_SESSION, twitterSessionJson);
        db.insertWithOnConflict(TWITTER_CONFIG_TABLE, null, tweetSessionValue,
                                SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }


    public TwitterSession loadTwitterSession() {
        TwitterSession session = null;
        String sessionJson = null;
        SQLiteDatabase db = this.getReadableDatabase();

        String getSessionQuery = "SELECT twitter_session FROM twitter_config";
        Cursor cursor = db.rawQuery(getSessionQuery, null);

        if (cursor.moveToFirst()) {
            sessionJson = cursor.getString(0);
        }
        cursor.close();

        if (!TextUtils.isEmpty(sessionJson))
            session = gson.fromJson(sessionJson, TwitterSession.class);

        return session;
    }


}
