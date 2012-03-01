package at.test.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import at.test.R;

public class MainMenuActivity extends TabActivity {

	TabHost mTabHost;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.main_menu);
		mTabHost = getTabHost();
		Intent intent;
		
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);

		intent = new Intent().setClass(this, RoomActivity.class);
		setupTab("Room", intent);

		intent = new Intent().setClass(this, UserInfoActivity.class);
		setupTab("User info", intent);

		intent = new Intent().setClass(this, FriendActivity.class);
		setupTab("Friends", intent);

		mTabHost.setCurrentTab(1);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.incoming, R.anim.outgoing);
	}

	private void setupTab(final String tag, final Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(setContent);
	}

	private static View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(R.layout.tab_item,
				null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}
}
