package at.test.data;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.ByteArrayBuffer;

import android.os.AsyncTask;
import android.util.Log;

/**
 * @author hoangnh
 * 
 */

public class RequestServer extends AsyncTask<String, Integer, String>{
	
	private IRequestServer delegate;
	
	enum REQUEST_TYPE{
		REQUEST_LOGIN,
		REQUEST_REGISTER
	};
	private REQUEST_TYPE requestType;
	
	public RequestServer(IRequestServer delegate){
		this.delegate = delegate;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		try {
			HttpParams para = new BasicHttpParams();
		    HttpConnectionParams.setConnectionTimeout(para, 15000);
		    HttpConnectionParams.setSoTimeout(para, 15000);
		    
			DefaultHttpClient httpClient = new DefaultHttpClient(para);
			HttpPost httpPost = new HttpPost(Config.REQUEST_SERVER_ADDR);
			// Add your data
			List<NameValuePair> nameValuePairs;
			
			switch (requestType) {
				case REQUEST_LOGIN:
					nameValuePairs = new ArrayList<NameValuePair>(3);
					nameValuePairs.add(new 
							BasicNameValuePair("message", "login"));
					nameValuePairs.add(new 
							BasicNameValuePair("username", params[0]));
					nameValuePairs.add(new 
							BasicNameValuePair("password", Utils.md5(params[1])));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					break;
				
				case REQUEST_REGISTER:
					nameValuePairs = new ArrayList<NameValuePair>(4);
					nameValuePairs.add(new 
							BasicNameValuePair("message", "register"));
					nameValuePairs.add(new 
							BasicNameValuePair("username", params[0]));
					nameValuePairs.add(new 
							BasicNameValuePair("password", Utils.md5(params[1])));
					nameValuePairs.add(new 
							BasicNameValuePair("email", params[2]));
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
					break;
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is, 8*1024);

			ByteArrayBuffer baf = new ByteArrayBuffer(1);

			int current = 0;

			while ((current = bis.read()) != -1) {

				baf.append((byte) current);

			}
			
			is.close();
			bis.close();

			/* Convert the Bytes read to a String. */

			result = new String(baf.toByteArray());

		} catch (UnsupportedEncodingException e) {
			result = e.getMessage();
		} catch (MalformedURLException e) {
			result = e.getMessage();
		} catch (IOException e) {
			result = e.getMessage();
		}	
		
		return result;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		delegate.onRequestComplete(result);
		Log.i("2", this.getStatus().toString());
	}
	
	public void login(String username, String password){
		this.requestType = REQUEST_TYPE.REQUEST_LOGIN;
		this.execute(username, password);
		Log.i("2", this.getStatus().toString());		
	}
	
	public void register(String username, String password, String email){
		this.requestType = REQUEST_TYPE.REQUEST_REGISTER;
		this.execute(username, password, email);
	}
}