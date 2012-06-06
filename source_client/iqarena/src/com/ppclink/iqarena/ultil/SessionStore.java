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

	private static boolean volume;
	private static final String KEY = "config";

	private static final String STORE_SESSION = "store_session";

	public static boolean getVolume() {
		return volume;
	}

	public static void restoreSession(Context context) {
		SharedPreferences savedSession = context.getSharedPreferences(KEY,
				Context.MODE_PRIVATE);

		volume = savedSession.getBoolean(STORE_SESSION, true);

	}
	
	public static boolean controlVolume(Context context, boolean control){
		Editor editor = context.getSharedPreferences(KEY, Context.MODE_PRIVATE)
				.edit();
		editor.putBoolean(STORE_SESSION, control);

		return editor.commit();
	}
}