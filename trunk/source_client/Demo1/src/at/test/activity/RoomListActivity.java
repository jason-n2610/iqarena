/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
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
public class RoomListActivity extends Activity implements IRequestServer,
		ICheckServer, OnClickListener {

	ListView mListViewRoom;
	Button mBtnNewRoom;

	String[] mStrListRoom;
	RequestServer mRequestServer = null;
	CheckServer mCheckServer = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.room_list);

		mListViewRoom = (ListView) findViewById(R.id.room_list_view);
		mBtnNewRoom = (Button) findViewById(R.id.room_btn_new_room);

		mBtnNewRoom.setOnClickListener(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (mCheckServer != null) {
			mCheckServer.cancel(true);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (mCheckServer != null) {
			if (mCheckServer.isCancelled()) {
				// task is cancel true
				mCheckServer = new CheckServer(this);
				mCheckServer.checkChangeRoom();
			}
		}
			mRequestServer = new RequestServer(this);
			mRequestServer.getListRoom();
			getParent().setProgressBarIndeterminateVisibility(true);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCheckServer != null) {
			mCheckServer.cancel(true);
		}
		if (mRequestServer != null) {
			mRequestServer.cancel(true);
		}
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void onRequestComplete(String sResult) {
		int len = sResult.length();
		if (sResult.contains("{") && len > 0) {
			int start = sResult.indexOf("{");
			sResult = sResult.substring(start, len);
			DataInfo.setData(sResult);
			int length = DataInfo.listRoom.size();
			// co room
			if (length > 0) {
				RoomListAdapter adapter = new RoomListAdapter(this, DataInfo.listRoom);
				mListViewRoom.setAdapter(adapter);
			}
		}

		getParent().setProgressBarIndeterminateVisibility(false);
		if (mCheckServer == null) {
			mCheckServer = new CheckServer(this);
			mCheckServer.checkChangeRoom();
		} else {
			mCheckServer.cancel(true);
			mCheckServer = new CheckServer(this);
			mCheckServer.checkChangeRoom();
		}
	}

	@Override
	public void onCheckServerComplete() {
		getParent().setProgressBarIndeterminateVisibility(true);
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_btn_new_room:
			Intent i = new Intent(getApplicationContext(),
					CreateNewRoomActivity.class);			
			i.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

			startActivity(i);
			break;

		default:
			break;
		}
	}
	
	public class RoomListAdapter extends ArrayAdapter<Room>{
		
		private LayoutInflater mInflater;
		private ArrayList<Room> mListRoom;

		public RoomListAdapter(Context context, ArrayList<Room> objects) {
			super(context, 1, objects);
			mInflater = LayoutInflater.from(context);
			mListRoom = objects;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			
			if (convertView == null){
				convertView = mInflater.inflate(R.layout.room_list_row, null);
				
				holder = new ViewHolder();
				holder.tvRoomName = (TextView) convertView.findViewById(R.id.tv_room_name);
				holder.tvOwnerName = (TextView) convertView.findViewById(R.id.tv_owner_name);
				holder.tvBetScore = (TextView) convertView.findViewById(R.id.tv_bet_score);
				
				convertView.setTag(holder);
			}
			else{
				holder = (ViewHolder) convertView.getTag();
			}
			
			Room room = mListRoom.get(position);
			
			holder.tvRoomName.setText(room.getRoomName());
			holder.tvOwnerName.setText(room.getOwnerName());
			holder.tvBetScore.setText(String.valueOf(room.getBetScore()));
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView tvRoomName, tvOwnerName, tvBetScore;
	}

}
