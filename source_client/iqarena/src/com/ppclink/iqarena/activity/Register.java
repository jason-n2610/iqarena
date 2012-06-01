package com.ppclink.iqarena.activity;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.AnalysisData;
import com.ppclink.iqarena.ultil.Utils;

public class Register extends Activity implements View.OnClickListener,
		IRequestServer {

	Button btnRegister;
	EditText etUsername, etPassword, etRePassword, etEmail;
	ConnectionManager requestServer;
	String strUsername, strPassword, strRePassword, strEmail;
	ScrollView svView;
	TextView tvNotice;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.register);
		setProgressBarIndeterminateVisibility(false);

		etUsername = (EditText) findViewById(R.id.editText1);
		etPassword = (EditText) findViewById(R.id.editText2);
		etRePassword = (EditText) findViewById(R.id.editText3);
		etEmail = (EditText) findViewById(R.id.editText4);
		tvNotice = (TextView) findViewById(R.id.tvRegisterResult);
		btnRegister = (Button) findViewById(R.id.btnRegister);
		svView = (ScrollView) findViewById(R.id.scrollView1);

		tvNotice.setText("");
		tvNotice.setVisibility(View.GONE);

		btnRegister.setOnClickListener(this);

		String path = getApplication().getExternalFilesDir(null).toString();
		File configFile = new File(path, Config.FILE_CONFIG_NAME);
		if (configFile.exists()) {
			configFile.delete();
		}
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister) {
			strUsername = etUsername.getText().toString().trim();
			strPassword = etPassword.getText().toString().trim();
			strRePassword = etRePassword.getText().toString().trim();
			strEmail = etEmail.getText().toString().trim();
			tvNotice.setText("");
			svView.fullScroll(View.FOCUS_UP);
			if (strUsername.equals("")) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("Username null");
				return;
			}
			if (strPassword.equals("")) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("password null");
				return;
			}
	
			if (strPassword.length() < 6) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("password phai lon hon 6 ki tu");
				return;
			}
	
			if (strRePassword.equals("")) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("re-password null");
				return;
			}
			if (strEmail.equals("")) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("email null");
				return;
			}
	
			if (!strPassword.equals(strRePassword)) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("Password <> Re-password");
				return;
			}
	
			if (!Utils.validateEmail(strEmail)) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("email invalid");
				return;
			}
	
			if (!Utils.validateString(strUsername)) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("username ko hop le, chi dc gom cac ki tu a-z 0-9 A-Z .");
				return;
			}
	
			if (!Utils.validateString(strPassword)) {
				tvNotice.setVisibility(View.VISIBLE);
				tvNotice.setText("password ko hop le, chi dc gom cac ki tu a-z 0-9 A-Z '.'");
				return;
			}
	
			tvNotice.setVisibility(View.GONE);
			getParent().setProgressBarIndeterminateVisibility(true);
			btnRegister.setEnabled(false);
			requestServer = new ConnectionManager(this);
			requestServer.register(strUsername, strPassword, strEmail);
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
				message = "Đăng kí thất bại";
			}
		}
		getParent().setProgressBarIndeterminateVisibility(false);
		btnRegister.setEnabled(true);
		tvNotice.setText(message);
		tvNotice.setVisibility(View.VISIBLE);
	}
}
