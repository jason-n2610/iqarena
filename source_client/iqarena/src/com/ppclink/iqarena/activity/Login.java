package com.ppclink.iqarena.activity;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.AnalysisData;
import com.ppclink.iqarena.ultil.Utils;

public class Login extends Activity implements View.OnClickListener,
		IRequestServer {

	Button btnLogin;
	CheckBox ckRemember;
	EditText etUsername, etPassword;
	ConnectionManager requestServer;
	TextView tvLoginResult;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.login);

		btnLogin = (Button) findViewById(R.id.login_login);
		tvLoginResult = (TextView) findViewById(R.id.login_result);
		etUsername = (EditText) findViewById(R.id.login_username);
		etPassword = (EditText) findViewById(R.id.login_password);
		ckRemember = (CheckBox) findViewById(R.id.ckRemember);

		tvLoginResult.setText("");
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.login_login:
			String strUsername = etUsername.getText().toString().trim();
			String strPassword = etPassword.getText().toString().trim();
			if (strUsername.equals("")) {
				tvLoginResult.setText("Username null");
				btnLogin.setEnabled(true);
				return;
			}
			if (strPassword.equals("")) {
				tvLoginResult.setText("Password null");
				btnLogin.setEnabled(true);
				return;
			}
			if (!Utils.validateString(strUsername)) {
				tvLoginResult.setText("Username chi dc gom [A-z][0-9].");
				btnLogin.setEnabled(true);
				return;
			}
			if (!Utils.validateString(strPassword)) {
				tvLoginResult.setText("Password chi dc gom [A-z][0-9].");
				btnLogin.setEnabled(true);
				return;
			}
			requestServer = new ConnectionManager(this);
			getParent().setProgressBarIndeterminateVisibility(true);
			btnLogin.setEnabled(false);
			requestServer.login(strUsername, strPassword);
			break;
		}
	
	}

	@Override
	public void onRequestComplete(String sResult) {
		String message = sResult;
		if (sResult != null) {
			sResult = sResult.trim();
			int length = sResult.length();
			// if co thong bao
			if (length > 0) {
				// kiem tra xem co thong tin tu server tra ve ko?
				if (sResult.contains("{")) {
					int start = sResult.indexOf("{");
					sResult = sResult.substring(start, length);
					boolean isSuccess = AnalysisData.analyze(sResult);
					message = AnalysisData.message;
					if (isSuccess) {
						if (AnalysisData.value) {
							// dung tai khoan
							// chuyen sang activity main menu
							// demo
							if (AnalysisData.userInfo != null) {
								if (ckRemember.isChecked()) {
									String username = etUsername.getText()
											.toString();
									String password = etPassword.getText()
											.toString();
									saveConfigFile(username, password);
								} 
								else{
									destroyConfigFile();
								}
								Intent intent = new Intent(
										getApplicationContext(),
										TabHostMain.class);
								startActivity(intent);
								overridePendingTransition(R.anim.incoming,
										R.anim.outgoing);
								message = "";
							}
						} else {
							// tai khoan khong hop le
							// in ra thong bao

						}
					}
				}
			}
			// if ko co thong bao
			else {
				message = "login thất bại";
			}
		}
		tvLoginResult.setText(message);
		btnLogin.setEnabled(true);
		getParent().setProgressBarIndeterminateVisibility(false);
	}
	
	/*
	 * save config file duoi dang nhieu String chuyen vao
	 */
	public boolean saveConfigFile(String str1, String str2) {
		boolean result = true;
		String path = getApplication().getExternalFilesDir(null).toString();
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
				buffWriter.write(str1);
				buffWriter.newLine();
				buffWriter.write(str2);
				buffWriter.flush();
				buffWriter.close();
			} catch (IOException e) {
				result = false;
				e.printStackTrace();
			}
		}

		return result;
	}

	public boolean destroyConfigFile() {
		String path = getApplication().getExternalFilesDir(null).toString();
		File configFile = new File(path, Config.FILE_CONFIG_NAME);
		if (configFile.exists()) {
			return configFile.delete();
		}
		return false;
	}

}