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

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import at.test.data.Config;
import at.test.delegate.ICheckServer;

/**
 * @author hoangnh
 * 
 */

public class CheckServer extends AsyncTask<String, Integer, String> {

	private ICheckServer delegate;

	enum REQUEST_TYPE {
		REQUEST_CHECK_CHANGE_ROOM,
		REQUEST_CHECK_MEMBERS_IN_ROOM
	};
	
	private int requestTime = 1000;
	
	String TAG = "CheckServer";
	String mPost = null;

	private REQUEST_TYPE requestType;

	public CheckServer(ICheckServer delegate) {
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

			case REQUEST_CHECK_CHANGE_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_CHECK_CHANGE_ROOM));
				break;			
			case REQUEST_CHECK_MEMBERS_IN_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_CHECK_MEMBERS_IN_ROOM));
				nameValuePairs.add(new BasicNameValuePair("room_id", params[0]));
				break;
			}
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
			String oldResult = null;
			boolean loop=false;
			while(!loop){
				Thread.sleep(requestTime);
				HttpResponse httpResponse = httpClient.execute(httpPost);
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
				Log.i(TAG, "result:"+result);
				if (oldResult == null){
					oldResult = result;
				}
				if (!result.equals(oldResult)){
					oldResult = result;
					mPost = result;
					publishProgress(1);					
				}
			}
			
		} catch (UnsupportedEncodingException e) {
			result = e.getMessage();
		} catch (MalformedURLException e) {
			result = e.getMessage();
		} catch (IOException e) {
			result = e.getMessage();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	@Override
	protected void onProgressUpdate(Integer... values) {
		super.onProgressUpdate(values);
		delegate.onCheckServerComplete(mPost);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		delegate.onCheckServerComplete(result);
	}

	// request check change room
	public void checkChangeRoom(){
		this.requestType = REQUEST_TYPE.REQUEST_CHECK_CHANGE_ROOM;
		this.execute();
	}
	
	// request check change members in room
	public void checkMembersInRoom(String strRoomID){
		this.requestType = REQUEST_TYPE.REQUEST_CHECK_MEMBERS_IN_ROOM;
		this.execute(strRoomID);
	}
}