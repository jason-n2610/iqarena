/**
 * 
 */
package com.ppclink.iqarena.ultil;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * @author hoangnh
 *         http://stackoverflow.com/questions/6824526/android-sharedpreferences
 *         -or-internal-storage
 */

public class SessionStore {

	private static boolean isSaved;
	private static final String KEY = "user_account";
	private static final String PASSWORD = "password";

	private static final String STORE_SESSION = "store_session";

	private static String strPassword;
	private static String strUsername;
	private static final String USERNAME = "username";

	public static String getPassword() {
		return strPassword;
	}

	public static boolean getSaved() {
		return isSaved;
	}

	public static String getUsername() {
		return strUsername;
	}

	public static void restoreSession(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);

		isSaved = savedSession.getBoolean(STORE_SESSION, false);
		strUsername = savedSession.getString(USERNAME, "username");
		strPassword = savedSession.getString(PASSWORD, "password");

	}

	public static boolean saveSession(Context context, boolean save,
			String username, String password) {
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(STORE_SESSION, save);
		editor.putString(USERNAME, username);
		editor.putString(PASSWORD, password);

		return editor.commit();
	}
}