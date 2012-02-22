package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import at.test.R;
import at.test.data.DataInfo;

public class MainMenuActivity extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		TextView tvInfo = (TextView) findViewById(R.id.textView1);
		tvInfo.setText("xin chào "+DataInfo.userInfo.getUsername());
	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		overridePendingTransition(R.anim.incoming, R.anim.outgoing);
	}
}
