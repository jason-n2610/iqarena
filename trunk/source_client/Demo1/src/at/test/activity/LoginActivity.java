package at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import at.test.R;
import at.test.data.IRequestServer;
import at.test.data.JsonData;
import at.test.data.RequestServer;
import at.test.data.Utils;

public class LoginActivity extends Activity implements View.OnClickListener, IRequestServer {
	
	Button btnRegister, btnLogin;
	TextView tvLoginResult;
	ProgressBar pbLoading;
	EditText etUsername, etPassword;
	RequestServer requestServer;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		btnRegister = (Button) findViewById(R.id.btnRegister);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		pbLoading = (ProgressBar) findViewById(R.id.progressBar1);
		tvLoginResult = (TextView) findViewById(R.id.tvLoginResult);
		etUsername = (EditText) findViewById(R.id.etLoginName);
		etPassword = (EditText) findViewById(R.id.etLoginPass);
		
		tvLoginResult.setText("");
		pbLoading.setVisibility(View.INVISIBLE);		
		btnRegister.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnLogin){
			pbLoading.setVisibility(View.VISIBLE);
			btnLogin.setEnabled(false);
			String strUsername = etUsername.getText().toString();
			String strPassword = etPassword.getText().toString();
			if (strUsername.equals("")){
				tvLoginResult.setText("Username null");
				pbLoading.setVisibility(View.INVISIBLE);
				btnLogin.setEnabled(true);
				return;
			}
			if (strPassword.equals("")){
				tvLoginResult.setText("Password null");
				pbLoading.setVisibility(View.INVISIBLE);
				btnLogin.setEnabled(true);
				return;
			}
			if (!Utils.validateString(strUsername)){
				tvLoginResult.setText("Username chi dc gom [A-z][0-9].");
				pbLoading.setVisibility(View.INVISIBLE);
				btnLogin.setEnabled(true);
				return;
			}
			if (!Utils.validateString(strPassword)){
				tvLoginResult.setText("Password chi dc gom [A-z][0-9].");
				pbLoading.setVisibility(View.INVISIBLE);
				btnLogin.setEnabled(true);
				return;
			}			
			requestServer = new RequestServer(this);
			requestServer.login(strUsername, strPassword);
		}
		else if (v.getId() == R.id.btnRegister){
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.incoming, R.anim.outgoing);
		}
		
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (sResult != null){
			sResult = sResult.trim();
			int length = sResult.length();
			// if co thong bao
			if (length > 0){
				if (sResult.contains("{")){
					int start = sResult.indexOf("{");
					sResult = sResult.substring(start, length);	
					boolean isSuccess = JsonData.getData(sResult);
					if (isSuccess){
						if (JsonData.message != null){
							sResult = JsonData.message;
						}
						if (!JsonData.value){
							// sai user
							if (JsonData.message != null){
								
							}
						}
						else{
							// success
							
						}
					}				
				}				
			}
			// if ko co thong bao
			else{
				sResult = "login that bai";
			}
		}
		tvLoginResult.setText(sResult);
		pbLoading.setVisibility(View.INVISIBLE);
		btnLogin.setEnabled(true);
	}
}