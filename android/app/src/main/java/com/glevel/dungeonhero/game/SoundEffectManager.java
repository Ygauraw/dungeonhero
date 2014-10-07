package com.glevel.dungeonhero.game;

import android.content.Context;
import android.util.Log;

import com.glevel.dungeonhero.game.logic.MapLogic;
import com.glevel.dungeonhero.models.Game;

import org.andengine.audio.sound.Sound;
import org.andengine.audio.sound.SoundFactory;
import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;

import java.util.HashMap;

public class SoundEffectManager {

    private static final String TAG = "SoundManager";
    private static final String ASSETS_PATH = "sfx/";
    private static final String SOUNDS_EXTENSION = ".ogg";
    private static final int EARS_DISTANCE = 100;// in pixels
    private static final int SILENCE_DISTANCE = 3200;// in pixels

    public static HashMap<String, Sound> sMfxMap = new HashMap<String, Sound>();

    private Context mContext;
    private boolean mSoundEnabled;
    private Camera mCamera;
    private Engine mEngine;

    public SoundEffectManager(Context context, int soundState) {
        mContext = context;
        mSoundEnabled = soundState == GameConstants.MusicState.on.ordinal();
    }

    public void init(Game game, Engine engine) {
        mEngine = engine;
        sMfxMap = new HashMap<String, Sound>();

        SoundFactory.setAssetBasePath(ASSETS_PATH);

        // TODO load all sounds from game
    }


    public void playGeoSound(String soundName, float x, float y) {
        playSound(soundName, false, -1, -1);
    }

    public void playSound(String soundName, boolean isLooped) {
        playSound(soundName, isLooped, -1, -1);
    }

    private void loadMfxFromAssets(String soundName) {
        if (sMfxMap.get(soundName) == null) {
            try {
                Sound mSound = SoundFactory.createSoundFromAsset(mEngine.getSoundManager(), mContext, soundName + SOUNDS_EXTENSION);
                sMfxMap.put(soundName, mSound);
            } catch (Exception e) {
                Log.w(TAG, e);
            }
        }
    }

    private void playSound(String soundName, boolean isLooped, float x, float y) {
        if (mSoundEnabled) {
            Sound sound = sMfxMap.get(soundName);
            if (sound != null) {
                sound.setLooping(isLooped);

                if (x >= 0 && y >= 0) {
                    // modify volume based on the distance from the sound to the camera
                    sound.setVolume(getVolumeFromSoundPosition(x, y));
                }

                sound.play();
            }
        }
    }

    private float getVolumeFromSoundPosition(float x, float y) {
        float distance = MapLogic.getDistanceBetween(x, y, mCamera.getCenterX() + EARS_DISTANCE, mCamera.getCenterY());
        return Math.max(0.0f, 1.0f - distance / SILENCE_DISTANCE);
    }

    public void onPause() {
        sMfxMap = new HashMap<String, Sound>();
    }

    public void setCamera(Camera camera) {
        this.mCamera = camera;
    }

}
