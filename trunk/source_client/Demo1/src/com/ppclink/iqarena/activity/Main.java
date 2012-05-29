package com.ppclink.iqarena.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.AnalysisData;
import com.ppclink.iqarena.ultil.SoundManager;

public class Main extends Activity implements OnClickListener, IRequestServer {

	DatabaseHelper mDataHelper;
	String tag = "Main";
	ConnectionManager mRequestServer;
	ProgressDialog mProgressDialog;
	ViewFlipper vfLayoutMain;
	
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
		Button btnOption = (Button) findViewById(R.id.main_btn_option);
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
//		if (player == null){
//			player = MediaPlayer.create(this, R.raw.main_theme2);
//			player.setLooping(true);
//			try {
//				player.prepare();
//			} catch (IllegalStateException e) {
//				e.printStackTrace();
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//			player.start();
//		}
//		else{
//			player.start();
//		}
	}

	@Override
	protected void onPause() {
		super.onPause();
//		if (player != null){
//			if (player.isPlaying()){
//				player.pause();
//				player.stop();
//			}
//		}
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
			break;
		case R.id.main_btn_option:
			break;
		case R.id.main_btn_help:
			break;
		case R.id.main_btn_about:
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