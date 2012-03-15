/**
 * 
 */
package at.test.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import at.test.R;
import at.test.connect.CheckServer;
import at.test.connect.RequestServer;
import at.test.data.DataInfo;
import at.test.delegate.ICheckServer;
import at.test.delegate.IRequestServer;
import at.test.object.Room;
import at.test.object.User;

/**
 * @author Administrator
 * 
 */
public class RoomListActivity extends Activity implements IRequestServer,
		ICheckServer, OnClickListener {
	
	ListView mListViewRoom;
	Button mBtnNewRoom;

	RequestServer mRequestServer = null;
	CheckServer mCheckServer = null;
	
	ArrayList<Room> alRooms = null;
	RoomListAdapter adapter = null;
	
	boolean isJoinRoom = false;
	String TAG = "RoomListActivity";
	String mRoomName;
	String mRoomOwnerName;
	// bien luu thong tin room_id khi user chon 1 room
	int roomID = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
                WindowManager.LayoutParams.FLAG_FULLSCREEN); 	
		setContentView(R.layout.room_list);

		mListViewRoom = (ListView) findViewById(R.id.room_list_view);
		mBtnNewRoom = (Button) findViewById(R.id.room_btn_new_room);

		mBtnNewRoom.setOnClickListener(this);
		alRooms = new ArrayList<Room>();
		adapter = new RoomListAdapter(getApplicationContext(), alRooms);
		mListViewRoom.setAdapter(adapter);
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

	// khi lay duoc du lieu get_list_room tu server
	@Override
	public void onRequestComplete(String sResult) {
		Log.i(TAG, "isJoinRoom: "+isJoinRoom);
		// truong hop join room
		if (isJoinRoom){
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				isJoinRoom = !isJoinRoom;
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				DataInfo.setData(sResult);
				if (DataInfo.value){
					Intent intent = new Intent(getApplicationContext(), RoomWaitingActivity.class);
					intent.putExtra("owner", false);
					intent.putExtra("room_id", roomID);
					intent.putExtra("room_name", mRoomName);
					intent.putExtra("owner_name", mRoomOwnerName);
					startActivity(intent);
				}
				else{
					Toast.makeText(this, "Sorry, join don't success", 300).show();
				}
			}
		}
		
		// truong hop xem danh sach room
		else{
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				DataInfo.setData(sResult);
				if (DataInfo.value){
					ArrayList<Room> alRoomTmp = DataInfo.mListRoom;
					if (alRoomTmp != null){
						int size = alRoomTmp.size();
						// co room
						if (size > 0) {
							alRooms.clear();
							for (int i=0; i<size; i++){
								alRooms.add(alRoomTmp.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				}
				else{
					alRooms.clear();
					adapter.notifyDataSetChanged();
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
	}

	@Override
	public void onCheckServerComplete(String result) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
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
			
			final Room room = mListRoom.get(position);
			
			holder.tvRoomName.setText(room.getRoomName());
			holder.tvOwnerName.setText(room.getOwnerName());
			holder.tvBetScore.setText(String.valueOf(room.getBetScore()));
			
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if (!mRequestServer.isCancelled()){
						mRequestServer.cancel(true);
					}
					mRoomName = room.getRoomName();
					roomID = room.getRoomId();
					mRoomOwnerName = room.getOwnerName();
					mRequestServer = new RequestServer(RoomListActivity.this);
					mRequestServer.joinRoom(String.valueOf(room.getRoomId()), String.valueOf(DataInfo.userInfo.getUserId()));
					isJoinRoom = true;
				}
			});
			
			return convertView;
		}
		
	}
	
	static class ViewHolder{
		TextView tvRoomName, tvOwnerName, tvBetScore;
	}

}
