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
import at.test.data.CheckServer;
import at.test.data.DataInfo;
import at.test.data.ICheckServer;
import at.test.data.IRequestServer;
import at.test.data.RequestServer;
import at.test.object.Room;

/**
 * @author Administrator
 * 
 */
public class RoomActivity extends Activity implements IRequestServer, ICheckServer {

	ListView mListViewRoom;
	String[] mStrListRoom;
	RequestServer mRequestServer;
	CheckServer mCheckServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		setContentView(R.layout.room);
		setProgressBarIndeterminateVisibility(true);
		
		mListViewRoom = (ListView) findViewById(R.id.room_list_view);
		
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
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
		mCheckServer = new CheckServer(this);
		mCheckServer.checkChangeRoom();
	}

	@Override
	public void onCheckServerComplete() {
		// TODO Auto-generated method stub
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
	}

}
