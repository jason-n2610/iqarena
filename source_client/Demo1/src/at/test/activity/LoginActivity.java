package at.test.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import at.test.R;
import at.test.connect.RequestServer;
import at.test.data.Config;
import at.test.data.DataInfo;
import at.test.data.Utils;
import at.test.delegate.IRequestServer;

public class LoginActivity extends Activity implements View.OnClickListener,
		IRequestServer, OnCheckedChangeListener {

	Button btnLogin;
	TextView tvLoginResult;
	EditText etUsername, etPassword;
	RequestServer requestServer;
	ProgressDialog progressDialog = null;
	CheckBox ckRemember;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.login);
		setProgressBarIndeterminateVisibility(false);

		Button btnRegister;

		btnRegister = (Button) findViewById(R.id.login_signup);
		btnLogin = (Button) findViewById(R.id.login_login);
		tvLoginResult = (TextView) findViewById(R.id.login_result);
		etUsername = (EditText) findViewById(R.id.login_username);
		etPassword = (EditText) findViewById(R.id.login_password);
		ckRemember = (CheckBox) findViewById(R.id.ckRemember);

		tvLoginResult.setText("");
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
		ckRemember.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		ArrayList<String> config = Config.getConfigFile();
		if (config != null) {
			if (config.size() == 2){
				etUsername.setText(config.get(0).trim());
				etPassword.setText(config.get(1).trim());
				ckRemember.setChecked(true);
				setProgressBarIndeterminateVisibility(true);
				btnLogin.setEnabled(false);
				requestServer = new RequestServer(this);
				requestServer.login(config.get(0).trim(), config.get(1).trim());
			}
		}
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
			requestServer = new RequestServer(this);
			setProgressBarIndeterminateVisibility(true);
			btnLogin.setEnabled(false);
			requestServer.login(strUsername, strPassword);
			break;
		case R.id.login_signup:
			if (requestServer != null){
				if (!requestServer.isCancelled()){
					requestServer.cancel(true);
				}
			}
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
			break;
		case R.id.ckRemember:
			tvLoginResult.setText("");
			if (!ckRemember.isChecked()) {
				Config.destroyConfigFile();
				ckRemember.setChecked(false);
			} else {
				ckRemember.setChecked(true);
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				if ((username.length() == 0) || (password.length() == 0)) {
					Toast.makeText(this, "Bạn chưa điền đầy đủ thông tin", 500)
							.show();
					ckRemember.setChecked(false);
				} else {
					boolean result = Config.saveConfigFile(username, password);
					if (!result) {
						tvLoginResult.setText(Config.getMessage());
					}
				}
			}
			break;
		}

	}

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		if (buttonView.getId() == R.id.ckRemember) {
			tvLoginResult.setText("");
			if (isChecked) {
				String username = etUsername.getText().toString();
				String password = etPassword.getText().toString();
				if ((username.length() == 0) || (password.length() == 0)) {
					Toast.makeText(this, "Bạn chưa điền đầy đủ thông tin", 500)
							.show();
					buttonView.setChecked(false);
				} else {
					boolean result = Config.saveConfigFile(username, password);
					if (!result) {
						tvLoginResult.setText(Config.getMessage());
					}
				}
			} else {
				Config.destroyConfigFile();
			}
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
					boolean isSuccess = DataInfo.setData(sResult);
					message = DataInfo.message;
					if (isSuccess) {
						if (DataInfo.value) {
							// dung tai khoan
							// chuyen sang activity main menu
							// demo
							if (DataInfo.userInfo != null) {
								Intent intent = new Intent(
										getApplicationContext(),
										TabHostMenuActivity.class);
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
		setProgressBarIndeterminateVisibility(false);
		if (progressDialog != null) {
			progressDialog.dismiss();
		}
	}

}