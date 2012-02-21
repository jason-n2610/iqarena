package at.test.data;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HeaderElement;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.util.ByteArrayBuffer;

import android.net.ParseException;
import android.os.AsyncTask;
import android.util.Log;

/**
 * @author hoangnh
 * 
 */

public class RequestServer extends AsyncTask<String, Integer, String> {

	private IRequestServer delegate;

	enum REQUEST_TYPE {
		REQUEST_LOGIN, REQUEST_REGISTER
	};

	private REQUEST_TYPE requestType;

	public RequestServer(IRequestServer delegate) {
		this.delegate = delegate;
	}

	@Override
	protected String doInBackground(String... params) {
		String result = null;
		try {
			 HttpParams para = new BasicHttpParams();
			 HttpConnectionParams.setConnectionTimeout(para, 15000);
			 HttpConnectionParams.setSoTimeout(para, 15000);
			 HttpProtocolParams.setContentCharset(para, "UTF-8");
			 HttpProtocolParams.setHttpElementCharset(para, "UTF-8");

			DefaultHttpClient httpClient = new DefaultHttpClient(para);
			HttpPost httpPost = new HttpPost(Config.REQUEST_SERVER_ADDR);
			// Add your data
			List<NameValuePair> nameValuePairs;

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
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				break;
			}

			HttpResponse httpResponse = httpClient.execute(httpPost);
			int responseCode = httpResponse.getStatusLine().getStatusCode();
			switch (responseCode) {
			case 200:
				HttpEntity entity = httpResponse.getEntity();

				Log.i("2", "charset: "+getContentCharSet(entity));
				InputStream is = entity.getContent();
				BufferedReader bis = new BufferedReader(new InputStreamReader(is, "utf-8"), 8 * 1024);
				ByteArrayBuffer baf = new ByteArrayBuffer(1);

				int current = 0;

				while ((current = bis.read()) != -1) {

					baf.append((byte) current);

				}

				is.close();
				bis.close();

				/* Convert the Bytes read to a String. */

				result = new String(baf.toByteArray());
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

	public void login(String username, String password) {
		this.requestType = REQUEST_TYPE.REQUEST_LOGIN;
		this.execute(username, password);
	}

	public void register(String username, String password, String email) {
		this.requestType = REQUEST_TYPE.REQUEST_REGISTER;
		this.execute(username, password, email);
	}

	public String getContentCharSet(final HttpEntity entity)
			throws ParseException {
		if (entity == null) {
			throw new IllegalArgumentException("HTTP entity may not be null");
		}
		String charset = null;
		if (entity.getContentType() != null) {
			HeaderElement values[] = entity.getContentType().getElements();
			if (values.length > 0) {
				NameValuePair param = values[0].getParameterByName("charset");
				if (param != null) {
					charset = param.getValue();
				}
			}
		}
		return charset;
	}

}