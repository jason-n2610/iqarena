/**
 * 
 */
package com.ppclink.iqarena.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.ultil.AnalysisData;

/**
 * @author Administrator
 * 
 */
public class UserInfo extends Activity {

	TextView mTvUserInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_info);
		mTvUserInfo = (TextView) findViewById(R.id.user_info_tv_info);
	}

	@Override
	protected void onResume() {
		super.onResume();
		mTvUserInfo.setText("Xin chào: " + AnalysisData.userInfo.getUsername()
				+ "\n" + "Điểm hiện tại của bạn: "
				+ (int) AnalysisData.userInfo.getScoreLevel());
	}
}
