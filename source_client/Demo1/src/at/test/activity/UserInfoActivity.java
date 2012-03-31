/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;
import at.test.R;
import at.test.data.DataInfo;

/**
 * @author Administrator
 *
 */
public class UserInfoActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		setContentView(R.layout.user_info);
		TextView tvUserInfo = (TextView) findViewById(R.id.user_info_tv_info);
		tvUserInfo.setText( "Xin chào: " + DataInfo.userInfo.getUsername() + "\n" +
							"Điểm hiện tại của bạn: " + (int)DataInfo.userInfo.getScoreLevel()
							);
	}
}
