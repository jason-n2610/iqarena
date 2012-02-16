package at.test;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.ByteArrayBuffer;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterTestActivity extends Activity implements View.OnClickListener{
	
	EditText etUsername, etPassword, etRePassword, etEmail;
	String strUsername, strPassword, strRePassword, strEmail;
	TextView tvNotice;
	Button btnRegister;
		
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        etUsername = (EditText) findViewById(R.id.editText1);
        etPassword = (EditText) findViewById(R.id.editText2);
        etRePassword = (EditText) findViewById(R.id.editText3);
        etEmail = (EditText) findViewById(R.id.editText4);
        tvNotice = (TextView) findViewById(R.id.textView5);
        btnRegister = (Button) findViewById(R.id.btnRegister);
        
        tvNotice.setText("");
        
        btnRegister.setOnClickListener(this);
    }


	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.btnRegister){
	        strUsername = etUsername.getText().toString();
	        strPassword = etPassword.getText().toString();
	        strRePassword = etRePassword.getText().toString();
	        strEmail = etEmail.getText().toString();
	        tvNotice.setText("");
	        Log.i("2", "register");
			if (strUsername.equals("")){
				tvNotice.setText("Username null");
				return;
			}
			if (strPassword.equals("")){
				tvNotice.setText("password null");
				return;
			}
			if (strRePassword.equals("")){
				tvNotice.setText("re-password null");
				return;
			}
			if (strEmail.equals("")){
				tvNotice.setText("email null");
				return;
			}
			
			if (!strPassword.equals(strRePassword)){
				tvNotice.setText("Password <> Re-password");
				return;
			}
			
			requestServer(strUsername, strPassword, strEmail);
		}
	}
	
	public String requestServer(String username, String password, String email) {
		String line = null;

		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(
					"http://192.168.0.120/iqarena/source_server/test.php");
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("message", "register"));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", password));
			nameValuePairs.add(new BasicNameValuePair("email", email));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {

				baf.append((byte) current);

			}

			/* Convert the Bytes read to a String. */

			line = new String(baf.toByteArray());


		} catch (UnsupportedEncodingException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			Log.i("2", "response: " + line);
		} catch (MalformedURLException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			Log.i("2", "response: " + line);
		} catch (IOException e) {
			line = "<results status=\"error\"><msg>Can't connect to server</msg></results>";
			Log.i("2", "response: " + line);
		}

		Log.i("2", "response: " + line);
		return line;

	}
	
	public String md5(String s) {
	    try {
	        // Create MD5 Hash
	        MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
	        digest.update(s.getBytes());
	        byte messageDigest[] = digest.digest();

	        // Create Hex String
	        StringBuffer hexString = new StringBuffer();
	        for (int i=0; i<messageDigest.length; i++)
	            hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
	        Log.i(getApplication().getClass().toString(), "md5 " + s + ":"+hexString.toString());
	        return hexString.toString();

	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    return "";
	}
}