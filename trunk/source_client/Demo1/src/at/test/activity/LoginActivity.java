package at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import at.test.R;
import at.test.connect.RequestServer;
import at.test.data.DataInfo;
import at.test.data.SessionStore;
import at.test.data.Utils;
import at.test.delegate.IRequestServer;

public class LoginActivity extends Activity implements View.OnClickListener,
		IRequestServer {

	Button btnLogin;
	TextView tvLoginResult;
	EditText etUsername, etPassword;
	RequestServer requestServer;
	CheckBox ckRemember;
	boolean isResume = false;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle extras = getIntent().getExtras();
		if (extras != null){
			// duoc goi khi nguoi dung an sign out
			isResume = extras.getBoolean("resume");
		}
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		getWindow().setWindowAnimations(android.R.anim.bounce_interpolator);
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
	}

	@Override
	protected void onResume() {
		super.onResume();
		
		SessionStore.restoreSession(getApplicationContext());
		if (SessionStore.getSaved()){
			etUsername.setText(SessionStore.getUsername());
			etPassword.setText(SessionStore.getPassword());
			ckRemember.setChecked(true);
			if (!isResume){
				setProgressBarIndeterminateVisibility(true);
				btnLogin.setEnabled(false);
				requestServer = new RequestServer(this);
				requestServer.login(SessionStore.getUsername(), SessionStore.getPassword());
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
		}

	}

	@Override
	public void onRequestComplete(String sResult) {
		Log.i("2", sResult);
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
								if (ckRemember.isChecked()){
									String username = etUsername.getText().toString();
									String password = etPassword.getText().toString();
									// save account 
									SessionStore.saveSession(getApplicationContext(), true, username, password);
								}
								else{
									SessionStore.saveSession(getApplicationContext(), false, "", "");
								}		
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
	}

}