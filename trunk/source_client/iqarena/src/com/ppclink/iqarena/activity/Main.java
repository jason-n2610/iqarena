package com.ppclink.iqarena.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.AnalysisData;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.SessionStore;

public class Main extends Activity implements OnClickListener, IRequestServer {

	DatabaseHelper mDataHelper;
	String tag = "Main";
	ConnectionManager mRequestServer;
	ProgressDialog mProgressDialog;
	ViewFlipper vfLayoutMain;
	PopupWindow mPopup;
	View mPopupView;
	boolean isSoundOn = true;
	Button btnOption;
	
	MediaPlayer player;
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button btnSingleOn = (Button) findViewById(R.id.main_btn_single_player_on);
		Button btnSingleOff = (Button) findViewById(R.id.main_btn_singler_player_off);
		Button btnMultiPlayer = (Button) findViewById(R.id.main_btn_multi_player);
		Button btnHelp = (Button) findViewById(R.id.main_btn_help);
		Button btnAbout = (Button) findViewById(R.id.main_btn_about);
		btnOption = (Button) findViewById(R.id.main_btn_option);
		Button ibUploadQues = (Button) findViewById(R.id.main_ib_upload_question);
		Button ibDownloadQues = (Button) findViewById(R.id.main_ib_download_question);
		vfLayoutMain = (ViewFlipper) findViewById(R.id.main_vf_main);

		btnAbout.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnSingleOn.setOnClickListener(this);
		btnOption.setOnClickListener(this);
		btnSingleOff.setOnClickListener(this);
		btnMultiPlayer.setOnClickListener(this);
		btnMultiPlayer.setOnClickListener(this);
		ibUploadQues.setOnClickListener(this);
		ibDownloadQues.setOnClickListener(this);
		
		// Set the hardware buttons to control the music
		this.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		
		vfLayoutMain.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.incoming));
		vfLayoutMain.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.outgoing));

		SessionStore.restoreSession(this);
		if (SessionStore.getVolume()){
			btnOption.setCompoundDrawablesWithIntrinsicBounds(null, 
					getResources().getDrawable(R.drawable.sound_on), null, null);
			isSoundOn = true;
		}
		else{
			btnOption.setCompoundDrawablesWithIntrinsicBounds(null, 
					getResources().getDrawable(R.drawable.sound_off), null, null);
			isSoundOn = false;
		}

//		new CountDownTimer(3000, 1000) {
//
//			@Override
//			public void onTick(long millisUntilFinished) {
//
//			}
//
//			@Override
//			public void onFinish() {
//				vfLayoutMain.showNext();
//			}
//		}.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		SessionStore.restoreSession(this);
		if (SessionStore.getVolume()){
			player = MediaPlayer.create(this, R.raw.main_theme2);
			player.setLooping(true);
			try {
				player.prepare();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			player.start();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		SessionStore.restoreSession(this);
		if (SessionStore.getVolume()){
			if (player != null){
				if (player.isPlaying()){
					player.pause();
					player.stop();
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_singler_player_off:
			Intent i = new Intent(this, LocalMode.class);
			startActivity(i);
			break;
		case R.id.main_ib_download_question:
			break;
		case R.id.main_ib_upload_question:
			Intent iUpload = new Intent(this, UploadQuestion.class);
			startActivity(iUpload);
			break;
		case R.id.main_btn_option:
			if (isSoundOn){
				SessionStore.controlVolume(this, false);
				if (player != null){
					if (player.isPlaying()){
						player.pause();
					}
				}
				btnOption.setCompoundDrawablesWithIntrinsicBounds(null, 
						getResources().getDrawable(R.drawable.sound_off), null, null);
			}
			else{
				SessionStore.controlVolume(this, true);
				if (player != null){
					player.start();
				}
				else{
					player = MediaPlayer.create(this, R.raw.main_theme2);
					player.setLooping(true);
					try {
						player.prepare();
					} catch (IllegalStateException e) {
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
					player.start();
				}
				btnOption.setCompoundDrawablesWithIntrinsicBounds(null, 
						getResources().getDrawable(R.drawable.sound_on), null, null);
			}
			isSoundOn = !isSoundOn;
			break;
		case R.id.main_btn_help:
			break;
		case R.id.main_btn_about:
			if (mPopup == null){
				LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
				View layout = inflater.inflate(R.layout.about, null);
				mPopup = new PopupWindow(this);
				mPopup.setWidth(320);
				mPopup.setHeight(320);
				mPopup.setOutsideTouchable(true);
				mPopup.setFocusable(true);
				mPopup.setBackgroundDrawable(new BitmapDrawable());
				mPopup.setContentView(layout);
				mPopup.showAsDropDown(findViewById(R.id.main_btn_about), -120, 0);
			}
			else{
				mPopup.showAsDropDown(findViewById(R.id.main_btn_about), -120, 0);
			}
			break;
		case R.id.main_btn_single_player_on:
			Intent iSingle = new Intent(this, SinglePlayer.class);
			startActivity(iSingle);
			break;
		case R.id.main_btn_multi_player:
			// kiem tra network
			if (!checkNetworkConnection()) {
				Toast.makeText(Main.this, "Please check network connection!",
						1000).show();
			} else {
				// kiem tra xem nguoi dung co da dang nhap lan truoc chua
				ArrayList<String> alAccount = getConfigFile();
				if (alAccount != null) {
					mRequestServer = new ConnectionManager(this);
					mRequestServer.login(alAccount.get(0), alAccount.get(1));
					mProgressDialog = ProgressDialog.show(this, "Info",
							"Connecting...");
				} else {
					Intent intent = new Intent(getApplicationContext(),
							TabHostLogin.class);
					startActivity(intent);
				}
			}
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		AnalysisData.analyze(sResult);
		// truong hop login thanh cong
		if (AnalysisData.value) {
			Intent intent = new Intent(getApplicationContext(),
					TabHostMain.class);
			startActivity(intent);
			overridePendingTransition(R.anim.incoming, R.anim.outgoing);
		}

		// truong hop login that bai
		// chuyen sang activity login/signup
		else {
			Intent intent = new Intent(getApplicationContext(),
					TabHostLogin.class);
			startActivity(intent);
		}
	}

	/*
	 * Ham tra ve list cac dong trong file config dong 1: username dong 2:
	 * password
	 */
	public ArrayList<String> getConfigFile() {
		ArrayList<String> result = null;
		String path = getApplication().getExternalFilesDir(null).toString();
		File configFile = new File(path, Config.FILE_CONFIG_NAME);
		if (configFile.exists()) {
			try {
				BufferedReader buffReader = new BufferedReader(new FileReader(
						configFile), 8 * 1024);
				String line;
				result = new ArrayList<String>();
				while ((line = buffReader.readLine()) != null) {
					result.add(line);
				}
			} catch (FileNotFoundException e) {
				return null;
			} catch (IOException e) {
				return null;
			}
		}
		return result;
	}

	public boolean checkNetworkConnection() {
		boolean result = true;
		ConnectivityManager conMgr = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo ni = conMgr.getActiveNetworkInfo();
		if (ni != null) {
			if (!ni.isAvailable()) {
				result = false;
			}
			if (!ni.isConnected()) {
				result = false;
			}
		} else {
			result = false;
		}
		return result;
	}

}
