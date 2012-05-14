package com.ppclink.iqarena.activity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.FilterResponse;

public class Main extends Activity implements OnClickListener, IRequestServer {

	DatabaseHelper mDataHelper;
	String tag = "Main";
	LinearLayout llMain, llNetworkMode;
	RequestServer mRequestServer;
	ProgressDialog mProgressDialog;
	ViewFlipper vfLayoutButton, vfLayoutMain;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		llMain = (LinearLayout) findViewById(R.id.main_ll_main);
		llNetworkMode = (LinearLayout) findViewById(R.id.main_ll_network_mode);

		Button btnLocalMode = (Button) findViewById(R.id.main_btn_local_mode);
		Button btnNetworkMode = (Button) findViewById(R.id.main_btn_network_mode);
		Button btnSinglePlayer = (Button) findViewById(R.id.main_btn_single_player);
		Button btnMultiPlayer = (Button) findViewById(R.id.main_btn_multi_player);
		Button btnHelp = (Button) findViewById(R.id.main_btn_help);
		Button btnAbout = (Button) findViewById(R.id.main_btn_about);
		Button btnOption = (Button) findViewById(R.id.main_btn_option);
		Button btnNetworkBack = (Button) findViewById(R.id.main_btn_network_back);
		vfLayoutButton = (ViewFlipper) findViewById(R.id.main_vf_btn);
		vfLayoutMain = (ViewFlipper) findViewById(R.id.main_vf_main);

		llMain.setVisibility(View.VISIBLE);
		llNetworkMode.setVisibility(View.GONE);

		btnAbout.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnLocalMode.setOnClickListener(this);
		btnOption.setOnClickListener(this);
		btnNetworkMode.setOnClickListener(this);
		btnSinglePlayer.setOnClickListener(this);
		btnMultiPlayer.setOnClickListener(this);
		btnNetworkBack.setOnClickListener(this);
		
		vfLayoutMain.setInAnimation(AnimationUtils.loadAnimation(this,
				R.anim.incoming));
		vfLayoutMain.setOutAnimation(AnimationUtils.loadAnimation(this,
				R.anim.outgoing));

		new CountDownTimer(3000, 1000) {

			@Override
			public void onTick(long millisUntilFinished) {

			}

			@Override
			public void onFinish() {
				vfLayoutMain.showNext();
			}
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.main_btn_local_mode:
			if (mDataHelper == null) {
				mDataHelper = new DatabaseHelper(this);
			}
			try {
				mDataHelper.createDataBase();
			} catch (IOException e) {
				Log.e(tag, e.getMessage());
			}

			try {
				mDataHelper.openDataBase();
			} catch (SQLException e) {
				Log.e(tag, e.getMessage());
			}
			ArrayList<QuestionLite> alQuestion = mDataHelper.getData();
			int size = alQuestion.size();
			Log.i(tag, "size: " + size);
			for (int i = 0; i < size; i++) {
				Log.i(tag, alQuestion.get(i).getQuesName());
			}
			mDataHelper.close();

			break;
		case R.id.main_btn_network_mode:
			vfLayoutButton.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_in));
			vfLayoutButton.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_up_out));
			vfLayoutButton.showNext();
			break;
		case R.id.main_btn_option:
			break;
		case R.id.main_btn_help:
			break;
		case R.id.main_btn_about:
			break;
		case R.id.main_btn_single_player:
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
					mRequestServer = new RequestServer(this);
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
		case R.id.main_btn_network_back:
			vfLayoutButton.setInAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_in));
			vfLayoutButton.setOutAnimation(AnimationUtils.loadAnimation(this,
					R.anim.push_down_out));
			vfLayoutButton.showPrevious();
			break;
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (mProgressDialog != null) {
			mProgressDialog.dismiss();
		}
		FilterResponse.filter(sResult);
		// truong hop login thanh cong
		if (FilterResponse.value) {
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
