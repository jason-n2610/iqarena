package at.test;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends Activity implements View.OnClickListener {

	EditText etUsername, etPassword, etRePassword, etEmail;
	String strUsername, strPassword, strRePassword, strEmail;
	TextView tvNotice;
	Button btnRegister;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		etUsername = (EditText) findViewById(R.id.editText1);
		etPassword = (EditText) findViewById(R.id.editText2);
		etRePassword = (EditText) findViewById(R.id.editText3);
		etEmail = (EditText) findViewById(R.id.editText4);
		tvNotice = (TextView) findViewById(R.id.tvRegisterResult);
		btnRegister = (Button) findViewById(R.id.btnRegister);

		tvNotice.setText("");

		btnRegister.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.incoming, R.anim.outgoing);
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister) {
			strUsername = etUsername.getText().toString();
			strPassword = etPassword.getText().toString();
			strRePassword = etRePassword.getText().toString();
			strEmail = etEmail.getText().toString();
			tvNotice.setText("");
			Log.i("2", "register");
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
			String result = RequestServer.register(strUsername, strPassword,
					strEmail);
			tvNotice.setText(result);
		}
	}
}
