/**
 * 
 */
package at.test.data;

import android.R.bool;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author hoangnh
 *         http://stackoverflow.com/questions/6824526/android-sharedpreferences
 *         -or-internal-storage
 */

public class SessionStore {

	private static final String STORE_SESSION = "store_session";
	private static final String USERNAME = "username";
	private static final String PASSWORD = "password";

	private static final String KEY = "user_account";

	private static boolean isSaved;
	private static String strUsername;
	private static String strPassword;

	public static boolean saveSession(Context context, boolean save, String username,
			String password) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(STORE_SESSION, save);
		editor.putString(USERNAME, username);
		editor.putString(PASSWORD, password);

		return editor.commit();
	}

	public static void restoreSession(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);
		
		isSaved = savedSession.getBoolean(STORE_SESSION, false);
		strUsername = savedSession.getString(USERNAME, "username");
		strPassword = savedSession.getString(PASSWORD, "password");

	}

	public static String getUsername() {
		return strUsername;
	}
	
	public static String getPassword(){
		return strPassword;
	}
	
	public static boolean getSaved(){
		return isSaved;
	}
}