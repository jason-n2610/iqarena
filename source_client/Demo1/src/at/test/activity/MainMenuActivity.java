package at.test.activity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import at.test.R;

public class MainMenuActivity extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tab_widget);
		TabHost tabHost = getTabHost();
		TabHost.TabSpec spec;
		Intent intent;

		intent = new Intent().setClass(this, RoomActivity.class);

		spec = tabHost.newTabSpec("rooms").setIndicator("Rooms")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, UserInfoActivity.class);
		spec = tabHost.newTabSpec("user_info").setIndicator("User Info")
				.setContent(intent);
		tabHost.addTab(spec);

		intent = new Intent().setClass(this, FriendActivity.class);
		spec = tabHost.newTabSpec("friends").setIndicator("Friends")
				.setContent(intent);
		tabHost.addTab(spec);

		tabHost.setCurrentTab(1);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.incoming, R.anim.outgoing);
	}
}
