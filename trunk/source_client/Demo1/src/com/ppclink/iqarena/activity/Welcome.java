/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.FilterResponse;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

/**
 * @author Administrator
 * 
 */
public class Welcome extends Activity implements IRequestServer {

	RequestServer mRequestServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.welcome);
		// lay ve thong tin cua user
		ArrayList<String> alAccount = getConfigFile();
		if (alAccount != null) {
			mRequestServer = new RequestServer(this);
			mRequestServer.login(alAccount.get(0), alAccount.get(1));
		}
		else{
			// doi 3s start activity login
			new CountDownTimer(3000, 1000) {
				
				@Override
				public void onTick(long millisUntilFinished) {
					
				}
				
				@Override
				public void onFinish() {
					Intent intent = new Intent(getApplicationContext(),
							TabHostLogin.class);
					startActivity(intent);
				}
			}.start();
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		try {
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
			else{
				Intent intent = new Intent(getApplicationContext(),
						TabHostLogin.class);
				startActivity(intent);
			}
		} catch (Exception e) {

		}
	}

	/*
	 * Ham tra ve list cac dong trong file config dong 1: username dong 2:
	 * password
	 */
	public ArrayList<String> getConfigFile() {
		ArrayList<String> result = null;
		File configFile = new File(Config.PATH_CONFIG, Config.FILE_CONFIG_NAME);
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

	/*
	 * Ham save config cua app save duoi dang cac dong dong 1: username dong 2:
	 * password
	 */
	public boolean saveConfigFile(ArrayList<String> content) {
		boolean result = true;
		String path = Config.PATH_CONFIG;
		File rootDir = new File(path);
		if (!(rootDir.exists() && rootDir.isDirectory())) {
			try {
				rootDir.mkdirs();
				result = true;
			} catch (SecurityException e) {
				result = false;
			}
		}
		if (result) {
			File configFile = new File(rootDir, Config.FILE_CONFIG_NAME);
			try {
				configFile.createNewFile();
				BufferedWriter buffWriter = new BufferedWriter(new FileWriter(
						configFile), 8 * 1024);
				int size = content.size();
				if (size > 0) {
					for (int i = 0; i < size - 1; i++) {
						buffWriter.write(content.get(i));
						buffWriter.newLine();
					}
					buffWriter.write(content.get(size - 1));
				}
				buffWriter.flush();
				buffWriter.close();
			} catch (IOException e) {
				result = false;
				e.printStackTrace();
			}
		}

		return result;
	}
}
