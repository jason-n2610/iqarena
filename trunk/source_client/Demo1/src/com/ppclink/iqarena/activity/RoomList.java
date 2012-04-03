/**
 * 
 */
package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.CheckServer;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.communication.RequestServer.REQUEST_TYPE;
import com.ppclink.iqarena.delegate.ICheckServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.Room;
import com.ppclink.iqarena.ultil.FilterResponse;

/**
 * @author Administrator
 * 
 */
public class RoomList extends Activity implements IRequestServer, ICheckServer,
		OnClickListener {

	RoomListAdapter adapter = null;
	ArrayList<Room> alRooms = null;

	Button mBtnNewRoom;
	CheckServer mCheckServer = null;

	ListView mListViewRoom;
	RequestServer mRequestServer = null;
	String mRoomName;
	String mRoomOwnerName;
	int mRoomTimePerQuestion;

	// bien luu thong tin room_id khi user chon 1 room
	String roomID;

	String TAG = "RoomListActivity";

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
		mListViewRoom.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (!mRequestServer.isCancelled()) {
					mRequestServer.cancel(true);
				}
				Room room = (Room) parent.getAdapter().getItem(position);
				mRoomName = room.getRoomName();
				roomID = String.valueOf(room.getRoomId());
				mRoomOwnerName = room.getOwnerName();
				mRoomTimePerQuestion = room.getTimePerQuestion();
				mRequestServer = new RequestServer(RoomList.this);
				mRequestServer.joinRoom(String.valueOf(room.getRoomId()),
						String.valueOf(FilterResponse.userInfo.getUserId()));
			}
		});
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
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.room_btn_new_room:
			Intent i = new Intent(getApplicationContext(), CreateNewRoom.class);
			startActivity(i);
			break;
	
		default:
			break;
		}
	}

	@Override
	public void onCheckServerComplete(String result) {
		getParent().setProgressBarIndeterminateVisibility(true);
		mRequestServer = new RequestServer(this);
		mRequestServer.getListRoom();
	}

	// khi lay duoc du lieu get_list_room tu server
	@Override
	public void onRequestComplete(String sResult) {
		// truong hop join room
		if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_JOIN_ROOM) {
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				FilterResponse.filter(sResult);
				if (FilterResponse.value) {
					Intent intent = new Intent(getApplicationContext(),
							RoomWaiting.class);
					intent.putExtra("owner", false);
					intent.putExtra("room_id", roomID);
					intent.putExtra("room_name", mRoomName);
					intent.putExtra("owner_name", mRoomOwnerName);
					intent.putExtra("member_id", FilterResponse.memberId);
					intent.putExtra("time_per_question", mRoomTimePerQuestion);
					startActivity(intent);
				} else {
					Toast.makeText(this, FilterResponse.message, 500).show();
				}
			}
		}

		// truong hop xem danh sach room
		else if (mRequestServer.getRequestType() == REQUEST_TYPE.REQUEST_GET_LIST_ROOM) {
			int len = sResult.length();
			if (sResult.contains("{") && len > 0) {
				int start = sResult.indexOf("{");
				sResult = sResult.substring(start, len);
				FilterResponse.filter(sResult);
				if (FilterResponse.value) {
					ArrayList<Room> alRoomTmp = FilterResponse.mListRoom;
					if (alRoomTmp != null) {
						int size = alRoomTmp.size();
						// co room
						if (size > 0) {
							alRooms.clear();
							for (int i = 0; i < size; i++) {
								alRooms.add(alRoomTmp.get(i));
							}
							adapter.notifyDataSetChanged();
						}
					}
				} else {
					alRooms.clear();
					adapter.notifyDataSetChanged();
				}
			}

			getParent().setProgressBarIndeterminateVisibility(false);
			if (mCheckServer == null) {
				mCheckServer = new CheckServer(this);
				mCheckServer.checkChangeRoom();
			}
			// else {
			// mCheckServer.cancel(true);
			// mCheckServer = new CheckServer(this);
			// mCheckServer.checkChangeRoom();
			// }
		}
	}

	public class RoomListAdapter extends ArrayAdapter<Room> {
	
		private LayoutInflater mInflater;
		private ArrayList<Room> mListRoom;
	
		public RoomListAdapter(Context context, ArrayList<Room> objects) {
			super(context, 1, objects);
			mInflater = LayoutInflater.from(context);
			mListRoom = objects;
		}
	
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
	
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.room_list_row, null);
	
				holder = new ViewHolder();
				holder.tvRoomName = (TextView) convertView
						.findViewById(R.id.tv_room_name);
				holder.tvOwnerName = (TextView) convertView
						.findViewById(R.id.tv_owner_name);
				holder.tvBetScore = (TextView) convertView
						.findViewById(R.id.tv_bet_score);
	
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, 40);
			convertView.setLayoutParams(lp);
	
			final Room room = mListRoom.get(position);
	
			holder.tvRoomName.setText(room.getRoomName());
			holder.tvOwnerName.setText(room.getOwnerName());
			holder.tvBetScore.setText(String.valueOf(room.getBetScore()));
	
			return convertView;
		}
	
		@Override
		public Room getItem(int position) {
			return super.getItem(position);
		}
	
	}

	static class ViewHolder {
		TextView tvRoomName, tvOwnerName, tvBetScore;
	}

}
