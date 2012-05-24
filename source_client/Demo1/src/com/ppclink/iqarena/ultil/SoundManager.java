package com.ppclink.iqarena.ultil;

import java.util.HashMap;

import com.ppclink.iqarena.R;

import android.app.AlertDialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

public class SoundManager {

	public final static int MAIN_THEME1 = 1;

	private SoundPool mSoundPool;
	private HashMap<Integer, Integer> mSoundPoolMap;
	private AudioManager mAudioManager;
	private Context mContext;
	static int index = 0;
	static boolean isEnable = false;

	public SoundManager(Context theContext) {
		mContext = theContext;
		mSoundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
		mSoundPoolMap = new HashMap<Integer, Integer>();

		mAudioManager = (AudioManager) mContext
				.getSystemService(Context.AUDIO_SERVICE);
		addSound(MAIN_THEME1, R.raw.main_theme1);
	}

	public void playSound() {
		if (isEnable) {
			int streamVolume = mAudioManager
					.getStreamVolume(AudioManager.STREAM_MUSIC);
			mSoundPool.play(mSoundPoolMap.get(index), streamVolume,
					streamVolume, 1, -1, 1f);
		}
	}
	
	public void playMainTheme(){
		int streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(MAIN_THEME1), streamVolume,
				streamVolume, 1, -1, 1f);		
	}

	public void playSound(int i) {
		int streamVolume = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		mSoundPool.play(mSoundPoolMap.get(i), streamVolume, streamVolume, 1, 0,
				1f);
	}

	public static void setSoundPlay(int i) {
		SoundManager.index = i;
	}

	public static void setEnable(boolean isEnable) {
		SoundManager.isEnable = isEnable;
	}

	public void addSound(int Index, int SoundID) {
		mSoundPoolMap.put(Index, mSoundPool.load(mContext, SoundID, 1));
	}

	public void releaseSound() {
		mSoundPool.release();
	}

	public AlertDialog.Builder showDialogSound() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

		return builder;
	}

}