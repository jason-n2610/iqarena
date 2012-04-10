package com.ppclink.iqarena.communication;

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
import android.util.Log;

import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.Config;
import com.ppclink.iqarena.ultil.FilterResponse;
import com.ppclink.iqarena.ultil.Utils;

/**
 * @author hoangnh
 * 
 */

public class RequestServer extends AsyncTask<String, Integer, String> {

	public enum REQUEST_TYPE {
		REQUEST_ANSWER_QUESTION, 
		REQUEST_CREATE_NEW_ROOM, 
		REQUEST_EXIT_ROOM, 
		REQUEST_GET_LIST_ROOM, 
		REQUEST_GET_MEMBERS_ANSWER,
		REQUEST_GET_MEMBERS_IN_ROOM, 
		REQUEST_REMOVE_MEMBER_IN_ROOM,
		REQUEST_GET_QUESTION, 
		REQUEST_JOIN_ROOM, 
		REQUEST_LOGIN, 
		REQUEST_PLAY_GAME, 
		REQUEST_REGISTER, 
		REQUEST_REMOVE_ROOM,
		REQUEST_MEMBER_READY
	}

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
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_LOGIN));
				nameValuePairs
						.add(new BasicNameValuePair("username", params[0]));
				nameValuePairs.add(new BasicNameValuePair("password", Utils
						.md5(params[1])));
				httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				break;
	
			case REQUEST_REGISTER:
				nameValuePairs = new ArrayList<NameValuePair>(4);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_REGISTER));
				nameValuePairs
						.add(new BasicNameValuePair("username", params[0]));
				nameValuePairs.add(new BasicNameValuePair("password", Utils
						.md5(params[1])));
				nameValuePairs.add(new BasicNameValuePair("email", params[2]));
				break;
	
			case REQUEST_GET_LIST_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(1);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_GET_LIST_ROOM));
				break;
			case REQUEST_CREATE_NEW_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(6);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_CREATE_NEW_ROOM));
				nameValuePairs.add(new BasicNameValuePair("room_name",
						params[0]));
				nameValuePairs.add(new BasicNameValuePair("owner_id", String
						.valueOf(FilterResponse.userInfo.getUserId())));
				nameValuePairs.add(new BasicNameValuePair("max_member",
						params[1]));
				nameValuePairs.add(new BasicNameValuePair("bet_score",
						params[2]));
				nameValuePairs.add(new BasicNameValuePair("time_per_question",
						params[3]));
				break;
			case REQUEST_REMOVE_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_REMOVE_ROOM));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				break;
			case REQUEST_JOIN_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_JOIN_ROOM));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				nameValuePairs
						.add(new BasicNameValuePair("user_id", params[1]));
				break;
			case REQUEST_EXIT_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_EXIT_ROOM));
				nameValuePairs
						.add(new BasicNameValuePair("member_id", params[0]));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[1]));
				break;
			case REQUEST_GET_MEMBERS_IN_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_GET_MEMBERS_IN_ROOM));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				break;
			case REQUEST_REMOVE_MEMBER_IN_ROOM:
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("message", "remove_member_in_room"));
				nameValuePairs
						.add(new BasicNameValuePair("member_id", params[0]));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[1]));
				break;
			case REQUEST_PLAY_GAME:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_PLAY_GAME));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				break;
	
			case REQUEST_GET_QUESTION:
				nameValuePairs = new ArrayList<NameValuePair>(2);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_GET_QUESTION));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				break;
	
			case REQUEST_ANSWER_QUESTION:
				nameValuePairs = new ArrayList<NameValuePair>(6);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_ANSWER_QUESTION));
				nameValuePairs.add(new BasicNameValuePair("member_id",
						params[0]));
				nameValuePairs.add(new BasicNameValuePair("user_id",
						String.valueOf(FilterResponse.userInfo.getUserId())));
				nameValuePairs.add(new BasicNameValuePair("room_id",
						params[1]));
				nameValuePairs.add(new BasicNameValuePair("question_id",
						params[2]));
				nameValuePairs.add(new BasicNameValuePair("question_answer",
						params[3]));
				break;
	
			case REQUEST_GET_MEMBERS_ANSWER:
				nameValuePairs = new ArrayList<NameValuePair>(6);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_GET_MEMBERS_ANSWER));
				nameValuePairs
						.add(new BasicNameValuePair("room_id", params[0]));
				nameValuePairs.add(new BasicNameValuePair("member_id",
						params[1]));
				nameValuePairs.add(new BasicNameValuePair("question_id",
						params[2]));
				nameValuePairs.add(new BasicNameValuePair("answer", 
						params[3]));
				nameValuePairs.add(new BasicNameValuePair("user_id",
						String.valueOf(FilterResponse.userInfo.getUserId())));
				break;
				
			case REQUEST_MEMBER_READY:
				nameValuePairs = new ArrayList<NameValuePair>(3);
				nameValuePairs.add(new BasicNameValuePair("message",
						Config.REQUEST_MEMBER_READY));
				nameValuePairs.add(new BasicNameValuePair("member_id", params[0]));
				nameValuePairs.add(new BasicNameValuePair("room_id", params[1]));
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
						is, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
				sb.append(bis.readLine() + "\n");
				String line = null;
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
		Log.i("REQUEST_SERVER", result);
	
		return result;
	}

	private IRequestServer delegate;;

	private REQUEST_TYPE requestType;

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		delegate.onRequestComplete(result);
	}

	// answer question
	public void answerQuestion(String strMemberId, String strRoomId, String strQuesId, String strAnswer) {
		this.requestType = REQUEST_TYPE.REQUEST_ANSWER_QUESTION;
		this.execute(strMemberId, strRoomId, strQuesId, strAnswer);
	}

	// create new room
	public void createNewRoom(String strRoomName, String strMaxMem,
			String strBetScore, String strTimePerQuestion) {
		this.requestType = REQUEST_TYPE.REQUEST_CREATE_NEW_ROOM;
		this.execute(strRoomName, strMaxMem, strBetScore, strTimePerQuestion);
	}

	// exit room
	public void exitRoom(String memberId, String roomId) {
		this.requestType = REQUEST_TYPE.REQUEST_EXIT_ROOM;
		this.execute(memberId, roomId);
	}

	// request get list room
	public void getListRoom() {
		this.requestType = REQUEST_TYPE.REQUEST_GET_LIST_ROOM;
		this.execute();
	}

	// get other members answer
	public void getMembersAnswer(String strRoomId, String strMemberId, 
			String strQuestionId, String strAnswer) {
		this.requestType = REQUEST_TYPE.REQUEST_GET_MEMBERS_ANSWER;
		this.execute(strRoomId, strMemberId, strQuestionId, strAnswer);
	}

	// get members in room
	public void getMembersInRoom(String strRoomID) {
		this.requestType = REQUEST_TYPE.REQUEST_GET_MEMBERS_IN_ROOM;
		this.execute(strRoomID);
	}
	
	// remove member in room (owner action)
	public void removeMemberInRoom(String memberId, String roomId){
		this.requestType = REQUEST_TYPE.REQUEST_REMOVE_MEMBER_IN_ROOM;
		this.execute(memberId, roomId);
	}

	// get question
	public void getQuestion(String strRoomId) {
		this.requestType = REQUEST_TYPE.REQUEST_GET_QUESTION;
		this.execute(strRoomId);
	}

	public REQUEST_TYPE getRequestType() {
		return requestType;
	}

	// join room
	public void joinRoom(String strRoomID, String strUserID) {
		this.requestType = REQUEST_TYPE.REQUEST_JOIN_ROOM;
		this.execute(strRoomID, strUserID);
	}

	// request login
	public void login(String username, String password) {
		this.requestType = REQUEST_TYPE.REQUEST_LOGIN;
		this.execute(username, password);
	}

	// play game
	public void playGame(String strRoomID) {
		this.requestType = REQUEST_TYPE.REQUEST_PLAY_GAME;
		this.execute(strRoomID);
	}

	// request register
	public void register(String username, String password, String email) {
		this.requestType = REQUEST_TYPE.REQUEST_REGISTER;
		this.execute(username, password, email);
	}

	// remove room
	public void removeRoom(String strRoomId) {
		this.requestType = REQUEST_TYPE.REQUEST_REMOVE_ROOM;
		this.execute(strRoomId);
	}
	
	// member ready for next question
	public void readyForGame(String memberID, String roomID){
		this.requestType = REQUEST_TYPE.REQUEST_MEMBER_READY;
		this.execute(memberID, roomID);
	}

	public void setRequestType(REQUEST_TYPE requestType) {
		this.requestType = requestType;
	}
}