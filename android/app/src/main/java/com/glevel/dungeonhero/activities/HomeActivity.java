package com.glevel.dungeonhero.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.glevel.dungeonhero.MyDatabase;
import com.glevel.dungeonhero.R;
import com.glevel.dungeonhero.activities.fragments.GameChooserFragment;
import com.glevel.dungeonhero.game.GameConstants;
import com.glevel.dungeonhero.utils.ApplicationUtils;
import com.glevel.dungeonhero.utils.MusicManager;
import com.glevel.dungeonhero.utils.MusicManager.Music;
import com.glevel.dungeonhero.utils.database.DatabaseHelper;
import com.google.android.gms.games.Games;
import com.google.example.games.basegameutils.BaseGameActivity;

import java.util.List;

public class HomeActivity extends BaseGameActivity implements OnClickListener {


    private static enum ScreenState {
        HOME, SOLO, SETTINGS
    }

    private static final int REQUEST_ACHIEVEMENTS = 0;

    private SharedPreferences mSharedPrefs;
    private ScreenState mScreenState = ScreenState.HOME;
    private DatabaseHelper mDbHelper;
    private List mLstGames;

    private Animation mMainButtonAnimationRightIn, mMainButtonAnimationRightOut, mMainButtonAnimationLeftIn, mMainButtonAnimationLeftOut;
    private Animation mFadeOutAnimation, mFadeInAnimation;
    private Button mNewGameButton, mSettingsButton, mLoadGameButton, mShareButton;
    private ViewGroup mSettingsLayout;
    private View mBackButton, mAppNameView;
    private Dialog mAboutDialog = null;
    private ImageView mStormsBg;
    private ViewGroup mLoginLayout;

    private Runnable mStormEffect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home);

        mSharedPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        mDbHelper = new MyDatabase(getApplicationContext());

        setupUI();

        ApplicationUtils.showRateDialogIfNeeded(this);
        ApplicationUtils.showAdvertisementIfNeeded(this);

        mLstGames = mDbHelper.getRepository(MyDatabase.Repositories.GAME.name()).getAll();

        showMainHomeButtons();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mStormsBg.removeCallbacks(mStormEffect);
        if (mAboutDialog != null) {
            mAboutDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mDbHelper.close();
    }

    @Override
    public void onClick(View v) {
        if (v.isShown()) {
            switch (v.getId()) {
                case R.id.newGameButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    startActivity(new Intent(this, NewGameActivity.class));
                    finish();
                    break;
                case R.id.loadGameButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    ApplicationUtils.openDialogFragment(this, new GameChooserFragment(), null);
                    break;
                case R.id.settingsButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    showSettings();
                    break;
                case R.id.backButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    onBackPressed();
                    break;
                case R.id.aboutButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    openAboutDialog();
                    break;
                case R.id.rateButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    ApplicationUtils.rateTheApp(this);
                    break;
                case R.id.shareButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    ApplicationUtils.startSharing(this, getString(R.string.share_subject, getString(R.string.app_name)),
                            getString(R.string.share_message, getPackageName()), 0);
                    break;
                case R.id.sign_in_button:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    beginUserInitiatedSignIn();
                    break;
                case R.id.sign_out_button:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    signOut();
                    showSignInButton();
                    break;
                case R.id.achievementsButton:
                    MusicManager.playSound(getApplicationContext(), R.raw.button_sound);
                    startActivityForResult(Games.Achievements.getAchievementsIntent(getApiClient()), REQUEST_ACHIEVEMENTS);
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        switch (mScreenState) {
            case HOME:
                super.onBackPressed();
                break;
            case SETTINGS:
                showMainHomeButtons();
                hideSettings();
                break;
            default:
                break;
        }
    }

    private void setupUI() {
        mStormsBg = (ImageView) findViewById(R.id.storms);

        mMainButtonAnimationRightIn = AnimationUtils.loadAnimation(this, R.anim.main_btn_right_in);
        mMainButtonAnimationLeftIn = AnimationUtils.loadAnimation(this, R.anim.main_btn_left_in);
        mMainButtonAnimationRightOut = AnimationUtils.loadAnimation(this, R.anim.main_btn_right_out);
        mMainButtonAnimationLeftOut = AnimationUtils.loadAnimation(this, R.anim.main_btn_left_out);

        mSettingsLayout = (ViewGroup) findViewById(R.id.settingsLayout);
        mLoginLayout = (ViewGroup) findViewById(R.id.loginLayout);

        mFadeOutAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_out);
        mFadeInAnimation = AnimationUtils.loadAnimation(this, R.anim.fade_in);

        mNewGameButton = (Button) findViewById(R.id.newGameButton);
        mNewGameButton.setOnClickListener(this);

        mLoadGameButton = (Button) findViewById(R.id.loadGameButton);
        mLoadGameButton.setOnClickListener(this);

        mSettingsButton = (Button) findViewById(R.id.settingsButton);
        mSettingsButton.setOnClickListener(this);

        mBackButton = (Button) findViewById(R.id.backButton);
        mBackButton.setOnClickListener(this);

        mAppNameView = (View) findViewById(R.id.appName);

        // volume radio buttons
        RadioGroup radioMusicVolume = (RadioGroup) findViewById(R.id.musicVolume);
        // update radio buttons states according to the music preference
        int musicVolume = mSharedPrefs.getInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, GameConstants.MusicStates.ON.ordinal());
        ((RadioButton) radioMusicVolume.getChildAt(musicVolume)).setChecked(true);
        radioMusicVolume.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // enable / disable sound in preferences
                GameConstants.MusicStates newMusicState = null;
                Editor editor = mSharedPrefs.edit();
                switch (checkedId) {
                    case R.id.musicOff:
                        newMusicState = GameConstants.MusicStates.OFF;
                        break;
                    case R.id.musicOn:
                        newMusicState = GameConstants.MusicStates.ON;

                        break;
                }
                editor.putInt(GameConstants.GAME_PREFS_KEY_MUSIC_VOLUME, newMusicState.ordinal());
                editor.commit();
                if (newMusicState == GameConstants.MusicStates.ON) {
                    MusicManager.start(HomeActivity.this, Music.MUSIC_MENU);
                } else {
                    MusicManager.release();
                }
            }
        });

        // settings buttons
        findViewById(R.id.aboutButton).setOnClickListener(this);
        findViewById(R.id.shareButton).setOnClickListener(this);
        findViewById(R.id.rateButton).setOnClickListener(this);

        // login / logout buttons
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        findViewById(R.id.sign_out_button).setOnClickListener(this);
        findViewById(R.id.achievementsButton).setOnClickListener(this);
    }

    private void openAboutDialog() {
        mAboutDialog = new Dialog(this, R.style.Dialog);
        mAboutDialog.setCancelable(true);
        mAboutDialog.setContentView(R.layout.dialog_about);
        // activate the dialog links
        TextView creditsTV = (TextView) mAboutDialog.findViewById(R.id.aboutCredits);
        creditsTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView contactTV = (TextView) mAboutDialog.findViewById(R.id.aboutContact);
        contactTV.setMovementMethod(LinkMovementMethod.getInstance());
        TextView sourcesTV = (TextView) mAboutDialog.findViewById(R.id.aboutSources);
        sourcesTV.setMovementMethod(LinkMovementMethod.getInstance());
        mAboutDialog.show();
    }

    private void showButton(View view, boolean fromRight) {
        if (fromRight) {
            view.startAnimation(mMainButtonAnimationRightIn);
        } else {
            view.startAnimation(mMainButtonAnimationLeftIn);
        }
        view.setVisibility(View.VISIBLE);
        view.setEnabled(true);
    }

    private void hideButton(View view, boolean toRight) {
        if (toRight) {
            view.startAnimation(mMainButtonAnimationRightOut);
        } else {
            view.startAnimation(mMainButtonAnimationLeftOut);
        }
        view.setVisibility(View.GONE);
        view.setEnabled(false);
    }

    private void showMainHomeButtons() {
        mScreenState = ScreenState.HOME;
        mBackButton.startAnimation(mFadeOutAnimation);
        mBackButton.setVisibility(View.GONE);
        mAppNameView.startAnimation(mFadeInAnimation);
        mAppNameView.setVisibility(View.VISIBLE);
        mLoginLayout.startAnimation(mFadeInAnimation);
        mLoginLayout.setVisibility(View.VISIBLE);
        showButton(mNewGameButton, true);
        showButton(mLoadGameButton, false);
        showButton(mSettingsButton, true);
        mLoadGameButton.setEnabled(mLstGames.size() > 0);
    }

    private void hideMainHomeButtons() {
        mAppNameView.startAnimation(mFadeOutAnimation);
        mAppNameView.setVisibility(View.GONE);
        mLoginLayout.startAnimation(mFadeOutAnimation);
        mLoginLayout.setVisibility(View.GONE);
        mBackButton.setVisibility(View.VISIBLE);
        mBackButton.startAnimation(mFadeInAnimation);
        hideButton(mNewGameButton, true);
        hideButton(mLoadGameButton, false);
        hideButton(mSettingsButton, true);
    }

    private void showSettings() {
        mScreenState = ScreenState.SETTINGS;
        mSettingsLayout.setVisibility(View.VISIBLE);
        mSettingsLayout.startAnimation(mFadeInAnimation);
        hideMainHomeButtons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // init storm effect
        mStormEffect = ApplicationUtils.addStormBackgroundAtmosphere(mStormsBg, 150, 50);
    }

    private void hideSettings() {
        mSettingsLayout.setVisibility(View.GONE);
        mSettingsLayout.startAnimation(mFadeOutAnimation);
    }

    @Override
    public void onSignInFailed() {
        showSignInButton();
    }

    @Override
    public void onSignInSucceeded() {
        // show sign-out button, hide the sign-in button
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        findViewById(R.id.achievementsButton).setVisibility(View.VISIBLE);
        findViewById(R.id.achievementsButton).startAnimation(mFadeInAnimation);
        findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    }

    private void showSignInButton() {
        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
        findViewById(R.id.achievementsButton).setVisibility(View.GONE);
        findViewById(R.id.sign_in_button).setVisibility(View.VISIBLE);
    }

}
