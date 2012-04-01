/**
 * 
 */
package com.ppclink.iqarena.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.ultil.FilterResponse;

/**
 * @author Administrator
 * 
 */
public class UserInfo extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.user_info);
		TextView tvUserInfo = (TextView) findViewById(R.id.user_info_tv_info);
		tvUserInfo.setText("Xin chào: " + FilterResponse.userInfo.getUsername()
				+ "\n" + "Điểm hiện tại của bạn: "
				+ (int) FilterResponse.userInfo.getScoreLevel());
	}
}
