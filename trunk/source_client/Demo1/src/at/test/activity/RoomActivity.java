/**
 * 
 */
package at.test.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
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
public class RoomActivity extends Activity implements IRequestServer, ICheckServer {

	ListView mListViewRoom;
	String[] mStrListRoom;
	RequestServer mRequestServer = null;
	CheckServer mCheckServer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.room);
		setProgressBarIndeterminateVisibility(true);
		
		mListViewRoom = (ListView) findViewById(R.id.room_list_view);
		
		if (savedInstanceState == null){
			mRequestServer = new RequestServer(this);
			mRequestServer.getListRoom();
		}
		else{
			if (mRequestServer == null){
				mRequestServer = new RequestServer(this);
				mRequestServer.getListRoom();
			}
			else{
				mRequestServer.cancel(true);
			}
		}
		Log.i("2", "onCreate()");
	}

	@Override
	protected void onStart() {
		super.onStart();
		Log.i("2", "onStart()");
	}

	@Override
	protected void onPause() {
		super.onPause();
		Log.i("2", "onPause()");
		if (mCheckServer != null){
			mCheckServer.cancel(true);
			Log.i("2", "onPause()");
		}
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
		Log.i("2", "onResume()");
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
		Log.i("2", "onDestroy()");
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
						android.R.layout.simple_list_item_1, android.R.id.text1,
						mStrListRoom);
				mListViewRoom.setAdapter(adapter);
			}
		}
		if (mCheckServer == null){
			mCheckServer = new CheckServer(this);
			mCheckServer.checkChangeRoom();
		}
		else{
			mCheckServer.cancel(true);
		}
	}

	@Override
	public void onCheckServerComplete() {
		// TODO Auto-generated method stub
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
	}

}
