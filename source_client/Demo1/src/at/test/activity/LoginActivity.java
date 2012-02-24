package at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.test.R;
import at.test.data.IRequestServer;
import at.test.data.DataInfo;
import at.test.data.RequestServer;
import at.test.data.Utils;

public class LoginActivity extends Activity implements View.OnClickListener,
		IRequestServer {
	
	Button btnRegister, btnLogin;
	TextView tvLoginResult;
	EditText etUsername, etPassword;
	RequestServer requestServer;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.login);		
        setProgressBarIndeterminateVisibility(false);

		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		tvLoginResult = (TextView) findViewById(R.id.tvLoginResult);
		etUsername = (EditText) findViewById(R.id.etLoginName);
		etPassword = (EditText) findViewById(R.id.etLoginPass);

		tvLoginResult.setText("");
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnLogin) {
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
		} else if (v.getId() == R.id.btnRegister) {
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
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
										MainMenuActivity.class);
								startActivity(intent);
								overridePendingTransition(R.anim.incoming,
										R.anim.outgoing);
								message="";
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