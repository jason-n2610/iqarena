/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import at.test.R;
import at.test.connect.CheckServer;
import at.test.connect.RequestServer;
import at.test.data.DataInfo;
import at.test.delegate.ICheckServer;
import at.test.delegate.IRequestServer;
import at.test.object.Room;

/**
 * @author Administrator
 * 
 */
public class RoomActivity extends Activity implements IRequestServer, ICheckServer, OnClickListener {

	ListView mListViewRoom;
	Button mBtnNewRoom;
	
	String[] mStrListRoom;
	RequestServer mRequestServer = null;
	CheckServer mCheckServer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room);
		
		mListViewRoom = (ListView) findViewById(R.id.room_list_view);
		mBtnNewRoom = (Button) findViewById(R.id.room_btn_new_room);
		
		mBtnNewRoom.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCheckServer != null){
			mCheckServer.cancel(true);
		}
//		getParent().setProgressBarIndeterminateVisibility(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCheckServer != null){
			if (mCheckServer.isCancelled()){
				// task is cancel true
				mCheckServer = new CheckServer(this);
				mCheckServer.checkChangeRoom();
			}
		}
		if (mRequestServer == null){
			mRequestServer = new RequestServer(this);
			mRequestServer.getListRoom();
			getParent().setProgressBarIndeterminateVisibility(true);
		}
		else{
			mRequestServer.cancel(true);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCheckServer != null){
			mCheckServer.cancel(true);
		}
		if (mRequestServer != null){
			mRequestServer.cancel(true);
		}
	}

	@Override
	public void onRequestComplete(String sResult) {
		int len = sResult.length();
		if (sResult.contains("{") && len>0){
			int start = sResult.indexOf("{");
			sResult = sResult.substring(start, len);
			DataInfo.setData(sResult);
			int length = DataInfo.listRoom.size();
			// co room
			if (length > 0){
				Log.i("2", "roomactivity, length:"+length);
				mStrListRoom = new String[length];
				for (int i=0; i<length; i++){
					Room room = DataInfo.listRoom.get(i);
					mStrListRoom[i] = room.getRoomName();
				}
				ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
						android.R.layout.simple_list_item_2, android.R.id.text1,
						mStrListRoom);
				mListViewRoom.setAdapter(adapter);
			}
		}

		getParent().setProgressBarIndeterminateVisibility(false);
		if (mCheckServer == null){
			mCheckServer = new CheckServer(this);
			mCheckServer.checkChangeRoom();
		}
		else{
			mCheckServer.cancel(true);
			mCheckServer = new CheckServer(this);
			mCheckServer.checkChangeRoom();
		}
	}

	@Override
	public void onCheckServerComplete() {
		// TODO Auto-generated method stub
		getParent().setProgressBarIndeterminateVisibility(true);
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_btn_new_room:
			
			break;

		default:
			break;
		}
	}

}
