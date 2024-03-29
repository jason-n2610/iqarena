package com.ppclink.iqarena.activity;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;

import com.ppclink.iqarena.R;

public class TabHostMain extends TabActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
//		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tab_host_menu);
		LinearLayout llMain = (LinearLayout) findViewById(R.id.tab_host_ll_main);
		llMain.setBackgroundResource(R.drawable.bg_app_sky_night);
		mTabHost = getTabHost();
		Intent intent;
	
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
	
		intent = new Intent().setClass(this, RoomList.class);
		setupTab("Room", intent);
	
		intent = new Intent().setClass(this, UserInfo.class);
		setupTab("User info", intent);
	
		mTabHost.setCurrentTab(0);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.incoming, R.anim.outgoing);
	}

	TabHost mTabHost;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0, 0, Menu.NONE, "Sign out");
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case 0:
			Intent intent = new Intent(getApplicationContext(),
					TabHostLogin.class);
			intent.putExtra("resume", true);
			startActivity(intent);
			break;
		case 1:

			break;

		default:
			onBackPressed();
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	private View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.tah_host_menu_item, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	private void setupTab(final String tag, final Intent intent) {
		View tabview = createTabView(mTabHost.getContext(), tag);
		TabSpec setContent = mTabHost.newTabSpec(tag).setIndicator(tabview)
				.setContent(intent);
		mTabHost.addTab(setContent);
	}
}
