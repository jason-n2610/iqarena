/**
 * 
 */
package at.test.data;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * @author hoangnh
 *
 */

public class JsonData {
	
	public static boolean value = false;
	public static String message = null;
	
	private static final String TAG = "JSONDATA";
	
	public static boolean getData(String input){
		boolean result = false;
		try {
			Log.i(TAG, input);
			JSONObject jObject = new JSONObject(input);
			String typeObj = jObject.getString("type");
			if (typeObj.equals("login")){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value){
					// truong hop login thanh cong
					// lay ve thong tin user
					
				}
			}
			else if (typeObj.equals("register")){
				
			}
			result = true;
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
		}
		return result;
	}
}
