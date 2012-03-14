/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import at.test.R;
import at.test.connect.RequestServer;
import at.test.delegate.IRequestServer;

/**
 * @author Administrator
 *
 */
public class RoomWaitingActivity extends Activity implements OnClickListener, IRequestServer{

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	
	// room id
	int roomID = 0;
	private String TAG = "RoomWaitingActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		
		// Nhan ve du lieu la owner hay member
		Bundle extras = getIntent().getExtras();
		boolean isOwner = extras.getBoolean("isOwner");
		roomID = extras.getInt("room_id");
		
		// hide statusbar
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		
		setContentView(R.layout.room_waiting);
		
		// init ui
		initUI(isOwner);
	}
	
	/*
	 * init ui
	 * Nhan vao la bien xac dinh xem la owner hay member
	 */
	void initUI(boolean isOwner){
		Button btnPlay = (Button) findViewById(R.id.room_waiting_btn_play);
		Button btnExit = (Button) findViewById(R.id.room_waiting_btn_exit);
		ListView lvMember = (ListView) findViewById(R.id.room_waiting_lv_member);
		TextView tvHeader = (TextView) findViewById(R.id.room_waiting_tv_header);
		
		if (!isOwner){
			btnPlay.setVisibility(View.GONE);
		}
		btnPlay.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	/*
	 * (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 * Event khi click vao button Play hay Exit
	 */
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_waiting_btn_play:
			
			break;
			
		// exit
		case R.id.room_waiting_btn_exit:
			RequestServer request = new RequestServer(this);
			request.removeRoom(String.valueOf(roomID));
			break;
			
		default:
			break;
		}
	}

	/*
	 * interface request server
	 * Tra ve du lieu nhan duoc tu server 
	 */
	@Override
	public void onRequestComplete(String sResult) {
		Log.i(TAG, "sResult: "+sResult);
		this.onBackPressed();
	}

}
