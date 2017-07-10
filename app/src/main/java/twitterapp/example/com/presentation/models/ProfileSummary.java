package twitterapp.example.com.presentation.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.twitter.sdk.android.core.models.User;

/**
 * ProfileSummary is a summary immutable class of the User object of the Tweet SDK.
 * That allows to map the user to the profile activity.
 */
public final class ProfileSummary implements Parcelable {

    private final long id;
    private final String name;
    private final String description;
    private final String profileImageUrl;
    private final int followersCount;
    private final int followingsCount;

    public ProfileSummary(User user) {
        this.id = user.id;
        this.name = user.name;
        this.description = user.description;
        this.profileImageUrl = user.profileImageUrlHttps;
        this.followersCount = user.followersCount;
        this.followingsCount = user.friendsCount;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public int getFollowersCount() {
        return followersCount;
    }

    public int getFollowingsCount() {
        return followingsCount;
    }

    protected ProfileSummary(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        profileImageUrl = in.readString();
        followersCount = in.readInt();
        followingsCount = in.readInt();
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeString(profileImageUrl);
        dest.writeInt(followersCount);
        dest.writeInt(followingsCount);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<ProfileSummary> CREATOR = new Parcelable.Creator<ProfileSummary>() {
        @Override
        public ProfileSummary createFromParcel(Parcel in) {
            return new ProfileSummary(in);
        }

        @Override
        public ProfileSummary[] newArray(int size) {
            return new ProfileSummary[size];
        }
    };

}
