/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import at.test.R;

/**
 * @author Administrator
 *
 */
public class RoomWaitingActivity extends Activity{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_waiting);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

}
