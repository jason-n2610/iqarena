package at.test.connect;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import android.os.AsyncTask;
import at.test.data.Config;
import at.test.data.Utils;
import at.test.delegate.IRequestServer;

/**
 * @author hoangnh
 * 
 */

public class RequestServer extends AsyncTask<String, Integer, String> {

	private IRequestServer delegate;

	enum REQUEST_TYPE {
		REQUEST_LOGIN, REQUEST_REGISTER, REQUEST_GET_LIST_ROOM, 
		REQUEST_CHECK_CHANGE_ROOM
	};

	private REQUEST_TYPE requestType;

	public RequestServer(IRequestServer delegate) {
		this.delegate = delegate;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		try {
			HttpParams httpParameters = new BasicHttpParams();
			HttpConnectionParams.setConnectionTimeout(httpParameters, 15000);
			HttpConnectionParams.setSoTimeout(httpParameters, 15000);

			HttpClient httpClient = new DefaultHttpClient(httpParameters);
			HttpPost httpPost = new HttpPost(Config.REQUEST_SERVER_ADDR);
			// post data
			List<NameValuePair> nameValuePairs = null;

			switch (requestType) {
			case REQUEST_LOGIN:
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("message", "login"));
				nameValuePairs
						.add(new BasicNameValuePair("username", params[0]));
				nameValuePairs.add(new BasicNameValuePair("password", Utils
						.md5(params[1])));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				break;

			case REQUEST_REGISTER:
				nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs
						.add(new BasicNameValuePair("message", "register"));
				nameValuePairs
						.add(new BasicNameValuePair("username", params[0]));
				nameValuePairs.add(new BasicNameValuePair("password", Utils
						.md5(params[1])));
				nameValuePairs.add(new BasicNameValuePair("email", params[2]));
				break;

			case REQUEST_GET_LIST_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("message",
						"get_list_room"));
				break;				
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				HttpEntity entity = httpResponse.getEntity();
				InputStream is = entity.getContent();
				BufferedReader bis = new BufferedReader(new InputStreamReader(
						is), 8);
				StringBuilder sb = new StringBuilder();
				sb.append(bis.readLine() + "\n");
				String line = "0";
				while ((line = bis.readLine()) != null) {
					sb.append(line + "\n");
				}
				bis.close();
				is.close();
				result = sb.toString();
				break;
			}
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
	}

	// request login
	public void login(String username, String password) {
		this.requestType = REQUEST_TYPE.REQUEST_LOGIN;
		this.execute(username, password);
	}

	// request register
	public void register(String username, String password, String email) {
		this.requestType = REQUEST_TYPE.REQUEST_REGISTER;
		this.execute(username, password, email);
	}

	// request get list room
	public void getListRoom() {
		this.requestType = REQUEST_TYPE.REQUEST_GET_LIST_ROOM;
		this.execute();
	}
}