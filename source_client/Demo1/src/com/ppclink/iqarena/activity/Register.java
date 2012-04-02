package com.ppclink.iqarena.activity;

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
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.FilterResponse;
import com.ppclink.iqarena.ultil.Utils;

public class Register extends Activity implements View.OnClickListener,
		IRequestServer {

	Button btnRegister;
	EditText etUsername, etPassword, etRePassword, etEmail;
	RequestServer requestServer;
	String strUsername, strPassword, strRePassword, strEmail;
	ScrollView svView;
	TextView tvNotice;

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
				tvNotice.setText("Username null");
				return;
			}
			if (strPassword.equals("")) {
				tvNotice.setText("password null");
				return;
			}

			if (strPassword.length() < 6) {
				tvNotice.setText("password phai lon hon 6 ki tu");
				return;
			}

			if (strRePassword.equals("")) {
				tvNotice.setText("re-password null");
				return;
			}
			if (strEmail.equals("")) {
				tvNotice.setText("email null");
				return;
			}

			if (!strPassword.equals(strRePassword)) {
				tvNotice.setText("Password <> Re-password");
				return;
			}

			if (!Utils.validateEmail(strEmail)) {
				tvNotice.setText("email invalid");
				return;
			}

			if (!Utils.validateString(strUsername)) {
				tvNotice.setText("username ko hop le, chi dc gom cac ki tu a-z 0-9 A-Z .");
				return;
			}

			if (!Utils.validateString(strPassword)) {
				tvNotice.setText("password ko hop le, chi dc gom cac ki tu a-z 0-9 A-Z '.'");
				return;
			}

			getParent().setProgressBarIndeterminateVisibility(true);
			btnRegister.setEnabled(false);
			requestServer = new RequestServer(this);
			requestServer.register(strUsername, strPassword, strEmail);
		}
	}

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

		btnRegister.setOnClickListener(this);
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
					boolean isSuccess = FilterResponse.filter(sResult);
					message = FilterResponse.message;
					if (isSuccess) {
						if (FilterResponse.value) {
							// dung tai khoan
							// chuyen sang activity main menu
							// demo
							if (FilterResponse.userInfo != null) {
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
	}
}