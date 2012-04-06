package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.ppclink.iqarena.R;
import com.ppclink.iqarena.communication.RequestServer;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.ultil.FilterResponse;

/**
 * @author hoangnh
 * 
 */
public class CreateNewRoom extends Activity implements OnClickListener,
		OnItemSelectedListener, IRequestServer {

	Button btnCreate;
	EditText etRoomName;
	Spinner spMaxNumber, spBetScore, spTimePerQuestion;
	TextView tvResult;

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_create:
			String strRoomName = etRoomName.getText().toString().trim();
			String strMaxMember = spMaxNumber.getSelectedItem().toString();
			String strBetScore = spBetScore.getSelectedItem().toString();
			String strTimePerQuestion = spTimePerQuestion.getSelectedItem()
					.toString();
			if (strRoomName.length() > 0) {
				RequestServer requestServer = new RequestServer(this);
				requestServer.createNewRoom(strRoomName, strMaxMember,
						strBetScore, strTimePerQuestion);
				btnCreate.setEnabled(false);
				setProgressBarIndeterminateVisibility(true);
			} else {
				tvResult.setVisibility(View.VISIBLE);
				tvResult.setText("Tên room không hợp lệ!");
			}
			break;

		case R.id.btn_cancel:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.create_new_room);
		setProgressBarIndeterminateVisibility(false);

		Button btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnCreate = (Button) findViewById(R.id.btn_create);
		etRoomName = (EditText) findViewById(R.id.et_room_name);
		spMaxNumber = (Spinner) findViewById(R.id.sp_max_member);
		spBetScore = (Spinner) findViewById(R.id.sp_bet_score);
		spTimePerQuestion = (Spinner) findViewById(R.id.sp_time_per_question);
		tvResult = (TextView) findViewById(R.id.tv_result);

		btnCancel.setOnClickListener(this);
		btnCreate.setOnClickListener(this);
		tvResult.setText("");
		tvResult.setVisibility(View.GONE);

		ArrayList<Integer> alMaxNumber = new ArrayList<Integer>();
		for (int i = 0; i < 9; i++) {
			alMaxNumber.add(i + 2);
		}
		ArrayList<Integer> alBetScore = new ArrayList<Integer>();
		for (int i = 5; i < 16; i++) {
			alBetScore.add(i * 100);
		}
		ArrayList<Integer> alTimePerQuestion = new ArrayList<Integer>();
		for (int i = 1; i < 7; i++) {
			alTimePerQuestion.add(i * 5);
		}

		ArrayAdapter<Integer> aaMaxNumber = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alMaxNumber);
		ArrayAdapter<Integer> aaBetScore = new ArrayAdapter<Integer>(this,
				android.R.layout.simple_spinner_item, alBetScore);
		ArrayAdapter<Integer> aaTimePerQuestion = new ArrayAdapter<Integer>(
				this, android.R.layout.simple_spinner_item, alTimePerQuestion);

		aaMaxNumber
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaBetScore
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		aaTimePerQuestion
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

		spMaxNumber.setAdapter(aaMaxNumber);
		spBetScore.setAdapter(aaBetScore);
		spTimePerQuestion.setAdapter(aaTimePerQuestion);

		spTimePerQuestion.setSelection(2);
		spMaxNumber.setSelection(3);
		
		spBetScore.setOnItemSelectedListener(this);
	}

	// interface IRequestServer
	@Override
	public void onRequestComplete(String sResult) {
		String strMessage = sResult;
		if (strMessage != null) {
			sResult = sResult.trim();
			int length = sResult.length();
			// if co thong bao
			if (length > 0) {
				// kiem tra xem co thong tin tu server tra ve ko?
				if (sResult.contains("{")) {
					int start = sResult.indexOf("{");
					sResult = sResult.substring(start, length);
					boolean isSuccess = FilterResponse.filter(sResult);
					strMessage = FilterResponse.message;
					String roomId = String.valueOf(FilterResponse.roomId);
					if (isSuccess) {
						// neu create thanh cong
						if (FilterResponse.value) {
							Intent intent = new Intent(getApplicationContext(),
									RoomWaiting.class);
							intent.putExtra("owner", true);
							intent.putExtra("room_id", roomId);
							intent.putExtra("room_name", etRoomName.getText()
									.toString().trim());
							intent.putExtra("owner_name",
									FilterResponse.userInfo.getUsername());
							intent.putExtra("member_id", FilterResponse.memberId);
							intent.putExtra("time_per_question", Integer
									.valueOf(spTimePerQuestion
											.getSelectedItem().toString()));
							startActivity(intent);
							strMessage = "";
						}
					}
					else{
						tvResult.setVisibility(View.VISIBLE);
						tvResult.setText(strMessage);
					}
				}
			}
		} else {
			strMessage = "Tạo room thất bại";
		}
		btnCreate.setEnabled(true);
		setProgressBarIndeterminateVisibility(false);
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int pos,
			long id) {
		int betScore = (pos+5)*100;
		if (betScore > (int)FilterResponse.userInfo.getScoreLevel()){
			btnCreate.setEnabled(false);
			tvResult.setVisibility(View.VISIBLE);
			tvResult.setText("Không đủ điểm để tham gia cược");
		}
		else{
			btnCreate.setEnabled(true);
			tvResult.setVisibility(View.GONE);
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		
	}
}
