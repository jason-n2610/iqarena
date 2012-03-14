/**
 * 
 */
package at.test.data;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import at.test.object.Room;
import at.test.object.User;

/**
 * @author hoangnh
 * 
 */

public class DataInfo {

	public static boolean value = false;
	public static String message = null;
	public static User userInfo = null;
	public static int roomId = 0;
	public static ArrayList<Room> mListRoom = new ArrayList<Room>();
	public static ArrayList<User> mListMemberInRoom = new ArrayList<User>();

	private static final String TAG = "JSONDATA";

	public static boolean setData(String input) {
		boolean result = false;
		try {
			Log.i(TAG, input);
			JSONObject jObject = new JSONObject(input);
			String typeMessage = jObject.getString("type");

			// message 'login'
			if (typeMessage.equals(Config.REQUEST_LOGIN)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop login thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: " + length);
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data
								.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);
					}

				}
			}

			// message 'register'
			else if (typeMessage.equals(Config.REQUEST_REGISTER)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop register thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: " + length);
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data
								.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);
					}

				}
			}
			
			// message 'create_new_room'
			else if (typeMessage.equals(Config.REQUEST_CREATE_NEW_ROOM)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				roomId = jObject.getInt("room_id");
			}
			
			// message 'get_list_room'
			else if (typeMessage.equals(Config.REQUEST_GET_LIST_ROOM)) {
				Log.i(TAG, "get list room");
				value = jObject.getBoolean("value");
				// truong hop co room tra ve
				if (value) {
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: " + length);
					mListRoom.clear();
					for (int i = 0; i < length; i++) {
						Room room;
						JSONObject json_data = jArray.getJSONObject(i);
						int roomId = json_data.getInt("room_id");
						String roomName = json_data.getString("room_name");
						int ownerId = json_data.getInt("owner_id");
						String ownerName = json_data.getString("username");
						int maxMember = json_data.getInt("max_member");
						int minMember = json_data.getInt("min_member");
						int status = json_data.getInt("status");
						int winScore = json_data.getInt("win_score");
						int numberOfMember = json_data
								.getInt("number_of_member");
						room = new Room(roomId, roomName, ownerId, ownerName, maxMember,
								minMember, winScore, status, numberOfMember);
						mListRoom.add(room);
					}
				}
				else{
					mListRoom.clear();
					message = jObject.getString("message");
				}
			}
			
			// message join room
			else if (typeMessage.equals(Config.REQUEST_JOIN_ROOM)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
			}
			
			// message exit room
			else if (typeMessage.equals(Config.REQUEST_EXIT_ROOM)){
				
			}
			
			// get members in room
			else if (typeMessage.equals(Config.REQUEST_GET_MEMBERS_IN_ROOM)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value){
					String jMembers = jObject.getString("members");
					JSONArray jArray = new JSONArray(jMembers);
					int length = jArray.length();
					Log.i(TAG, "length: " + length);
					mListMemberInRoom.clear();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data
								.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");

						User user = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);
						
						mListMemberInRoom.add(user);
					}
				}
				else{
					mListMemberInRoom.clear();
				}
			}
			
			result = true;
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
			result = false;
		}
		return result;
	}
}
