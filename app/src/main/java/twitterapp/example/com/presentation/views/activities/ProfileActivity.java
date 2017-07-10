package twitterapp.example.com.presentation.views.activities;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import butterknife.BindView;
import butterknife.ButterKnife;
import twitterapp.example.com.twitterapp.R;
import twitterapp.example.com.presentation.models.ProfileSummary;

import static twitterapp.example.com.Constants.PROFILE_CLICKED;
/**
 * Activity that displays the profile information.
 */
public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.profile_toolbar) Toolbar profileToolbar;
    @BindView(R.id.profile_card) CardView profileCard;
    @BindView(R.id.profile_avatar) ImageView profileAvatar;
    @BindView(R.id.profile_name) TextView  profileName;
    @BindView(R.id.profile_description) TextView  profileDescription;
    @BindView(R.id.profile_following) TextView profileFollowing;
    @BindView(R.id.profile_followers) TextView profileFollowers;
    private ProfileSummary profileSummary;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            profileSummary = extras.getParcelable(PROFILE_CLICKED);

        if (profileSummary == null)
            // Not showing the activity if there is no profile summary.
            finish();

        if (profileToolbar != null) {
            profileToolbar.setTitle(profileSummary.getName());
            setSupportActionBar(profileToolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        Glide.with(this)
                .load(profileSummary.getProfileImageUrl())
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .into(profileAvatar);

        profileName.setText(profileSummary.getName());
        profileDescription.setText(profileSummary.getDescription());
        profileFollowing.setText( getString(R.string.profile_following_title, profileSummary.getFollowingsCount()));
        profileFollowers.setText(getString(R.string.profile_followers_title, profileSummary.getFollowersCount()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
