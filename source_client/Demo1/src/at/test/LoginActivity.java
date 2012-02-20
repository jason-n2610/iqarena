package at.test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class LoginActivity extends Activity implements View.OnClickListener {
	
	Button btnRegister, btnLogin;
	TextView tvLoginResult;
	ProgressBar pbLoading;
	EditText etUsername, etPassword;
	
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
			String strUsername = etUsername.getText().toString();
			String strPassword = etPassword.getText().toString();
			if (strUsername.equals("")){
				tvLoginResult.setText("Username null");
				pbLoading.setVisibility(View.INVISIBLE);
				return;
			}
			if (strPassword.equals("")){
				tvLoginResult.setText("Password null");
				pbLoading.setVisibility(View.INVISIBLE);
				return;
			}
			if (!Utils.validateString(strUsername)){
				tvLoginResult.setText("Username chi dc gom [A-z][0-9].");
				pbLoading.setVisibility(View.INVISIBLE);
				return;
			}
			if (!Utils.validateString(strPassword)){
				tvLoginResult.setText("Password chi dc gom [A-z][0-9].");
				pbLoading.setVisibility(View.INVISIBLE);
				return;
			}			
			String result = RequestServer.login(strUsername, strPassword);
			tvLoginResult.setText(result);			
			pbLoading.setVisibility(View.INVISIBLE);
		}
		else if (v.getId() == R.id.btnRegister){
			Intent intent = new Intent(getApplicationContext(),
					RegisterActivity.class);
			startActivity(intent);
			overridePendingTransition(R.anim.incoming, R.anim.outgoing);
		}
		
	}
}