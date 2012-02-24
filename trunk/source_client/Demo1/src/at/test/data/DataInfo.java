/**
 * 
 */
package at.test.data;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;
import at.test.object.User;

/**
 * @author hoangnh
 * 
 */

public class DataInfo {

	public static boolean value = false;
	public static String message = null;
	public static User userInfo = null;
	private static final String TAG = "JSONDATA";

	public static boolean setData(String input) {
		boolean result = false;
		try {
			Log.i(TAG, input);
			JSONObject jObject = new JSONObject(input);
			String typeMessage = jObject.getString("type");
			if (typeMessage.equals("login")) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop login thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: "+length);
					for (int i=0; i<length; i++){
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email, scoreLevel,
								registedDate, powerUser, money);
					}

				}
			} else if (typeMessage.equals("register")) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop register thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: "+length);
					for (int i=0; i<length; i++){
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email, scoreLevel,
								registedDate, powerUser, money);
					}

				}
			}
			result = true;
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
		}
		return result;
	}
}
