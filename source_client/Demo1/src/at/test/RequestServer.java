package at.test;

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
import org.apache.http.util.ByteArrayBuffer;

/**
 * @author hoangnh
 * 
 */
public class RequestServer {	

	public static String register(String username, String password, String email) {
		String line = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Config.REQUEST_SERVER_ADDR);
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(4);
			nameValuePairs.add(new BasicNameValuePair("message", Config.REQUEST_REGISTER));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", Utils.md5(password)));
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

			is.close();
			bis.close();
			
			line = new String(baf.toByteArray());

		} catch (UnsupportedEncodingException e) {
			line = e.getMessage();
		} catch (MalformedURLException e) {
			line = e.getMessage();
		} catch (IOException e) {
			line = e.getMessage();
		}
		return line;
	}
	
	public static String login(String username, String password) {
		String line = null;
		try {

			DefaultHttpClient httpClient = new DefaultHttpClient();
			HttpPost httpPost = new HttpPost(Config.REQUEST_SERVER_ADDR);
			// Add your data
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(3);
			nameValuePairs.add(new BasicNameValuePair("message", Config.REQUEST_LOGIN));
			nameValuePairs.add(new BasicNameValuePair("username", username));
			nameValuePairs.add(new BasicNameValuePair("password", Utils.md5(password)));
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

			HttpResponse httpResponse = httpClient.execute(httpPost);
			InputStream is = httpResponse.getEntity().getContent();
			BufferedInputStream bis = new BufferedInputStream(is);

			ByteArrayBuffer baf = new ByteArrayBuffer(20);

			int current = 0;

			while ((current = bis.read()) != -1) {

				baf.append((byte) current);

			}
			
			is.close();
			bis.close();

			/* Convert the Bytes read to a String. */

			line = new String(baf.toByteArray());

		} catch (UnsupportedEncodingException e) {
			line = e.getMessage();
		} catch (MalformedURLException e) {
			line = e.getMessage();
		} catch (IOException e) {
			line = e.getMessage();
		}
		return line;
	}
}
