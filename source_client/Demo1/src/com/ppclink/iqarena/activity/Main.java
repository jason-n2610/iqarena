package com.ppclink.iqarena.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.ppclink.iqarena.R;

public class Main extends Activity implements OnClickListener{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		Button btnLocalMode = (Button) findViewById(R.id.main_btn_local_mode);
		Button btnNetworkMode = (Button) findViewById(R.id.main_btn_network_mode);
		Button btnHelp = (Button) findViewById(R.id.main_btn_help);
		Button btnAbout = (Button) findViewById(R.id.main_btn_about);
		Button btnOption = (Button) findViewById(R.id.main_btn_option);
		
		btnAbout.setOnClickListener(this);
		btnHelp.setOnClickListener(this);
		btnLocalMode.setOnClickListener(this);
		btnOption.setOnClickListener(this);
		btnNetworkMode.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.main_btn_local_mode:
			break;
		case R.id.main_btn_network_mode:
			break;
		case R.id.main_btn_option:
			break;
		case R.id.main_btn_help:
			break;
		case R.id.main_btn_about:
			break;
		}
	}
	
}
