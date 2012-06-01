package com.ppclink.iqarena.activity;

import com.ppclink.iqarena.R;

import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.TabSpec;

public class TabHostLogin extends TabActivity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.tab_host_menu);
		LinearLayout main = (LinearLayout) findViewById(R.id.tab_host_ll_main);
		main.setBackgroundDrawable(getResources().getDrawable(R.drawable.bg_app_fire));
		mTabHost = getTabHost();
		Intent intent;
	
		mTabHost.getTabWidget().setDividerDrawable(R.drawable.tab_divider);
	
		intent = new Intent().setClass(this, Login.class);
		setupTab("Login", intent);
	
		intent = new Intent().setClass(this, Register.class);
		setupTab("Sign up", intent);
	
		mTabHost.setCurrentTab(0);
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

	private View createTabView(final Context context, final String text) {
		View view = LayoutInflater.from(context).inflate(
				R.layout.tah_host_menu_item, null);
		TextView tv = (TextView) view.findViewById(R.id.tabsText);
		tv.setText(text);
		return view;
	}

	TabHost mTabHost;
}