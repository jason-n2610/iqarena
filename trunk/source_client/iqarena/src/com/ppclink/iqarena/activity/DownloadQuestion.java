package com.ppclink.iqarena.activity;

import java.util.ArrayList;

import com.ppclink.iqarena.R;
import com.ppclink.iqarena.activity.RoomList.ViewHolder;
import com.ppclink.iqarena.connection.ConnectionManager;
import com.ppclink.iqarena.connection.ConnectionManager.REQUEST_TYPE;
import com.ppclink.iqarena.database.DatabaseHelper;
import com.ppclink.iqarena.delegate.IRequestServer;
import com.ppclink.iqarena.object.Category;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.object.Room;
import com.ppclink.iqarena.ultil.AnalysisData;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class DownloadQuestion extends Activity implements IRequestServer{

	ConnectionManager mConenction;
	ListView lvCategory;
	ProgressDialog progressDialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.download_question);
		
		lvCategory = (ListView) findViewById(R.id.category_list_view);
		lvCategory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if (!mConenction.isCancelled()) {
					mConenction.cancel(true);
				}
				Category category = (Category) parent.getAdapter().
						getItem(position);
				String category_id = category.getCategory_id();
				mConenction = new ConnectionManager(DownloadQuestion.this);
				mConenction.downloadCategory(category_id);
				progressDialog = ProgressDialog.show(DownloadQuestion.this, 
"Download Questions", "downloading...");
			}
		});
		Toast.makeText(this, "Select a category to download", 
				Toast.LENGTH_LONG).show();
	}
	


	@Override
	protected void onPause() {
		super.onPause();
		if (mConenction != null){
			if (!mConenction.isCancelled()){
				mConenction.cancel(true);
			}
		}
	}



	@Override
	protected void onResume() {
		super.onResume();
		if (mConenction != null){
			if (!mConenction.isCancelled()){
				mConenction.cancel(true);
			}
		}
		mConenction = new ConnectionManager(this);
		mConenction.getCategory();
	}



	public class CategoryAdapter extends ArrayAdapter<Category> {
	
		private LayoutInflater mInflater;
		private ArrayList<Category> alCategory;
	
		public CategoryAdapter(Context context, ArrayList<Category> objects) {
			super(context, 1, objects);
			mInflater = LayoutInflater.from(context);
			alCategory = objects;
		}
	
		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			ViewHolder holder;
	
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.room_list_row, null);
	
				holder = new ViewHolder();
				holder.tvCategoryName = (TextView) convertView
						.findViewById(R.id.tv_room_name);
				holder.tvDateCreated = (TextView) convertView
						.findViewById(R.id.tv_owner_name);
				holder.tvDescrible = (TextView) convertView
						.findViewById(R.id.tv_bet_score);
	
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
	
			AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
					android.view.ViewGroup.LayoutParams.FILL_PARENT, 40);
			convertView.setLayoutParams(lp);
	
			Category category = alCategory.get(position);
	
			holder.tvCategoryName.setText(category.getCategory_name());
			holder.tvDateCreated.setText(category.getDate_created());
			holder.tvDescrible.setText(category.getDescrible());
	
			return convertView;
		}	
		
		@Override
		public Category getItem(int position) {
			return super.getItem(position);
		}
	}

	static class ViewHolder {
		TextView tvCategoryName, tvDateCreated, tvDescrible;
	}

	@Override
	public void onRequestComplete(String sResult) {
		if (sResult != null){
			if (mConenction.getRequestType() == 
					REQUEST_TYPE.REQUEST_GET_CATEGORY){
				if (sResult.contains("{")){
					AnalysisData.analyze(sResult);
					if (AnalysisData.value){
						ArrayList<Category> listCategory = 
								AnalysisData.mListCategory;
						if (listCategory != null){
							CategoryAdapter adapter = new CategoryAdapter(
									DownloadQuestion.this, listCategory);
							lvCategory.setAdapter(adapter);
						}
					}
					else{
						Toast.makeText(this, AnalysisData.message, Toast.LENGTH_LONG).show();
					}
				}
				else{
					Toast.makeText(this, AnalysisData.message, Toast.LENGTH_LONG).show();
				}
			}
			else if (mConenction.getRequestType() == 
					REQUEST_TYPE.REQUEST_DOWNLOAD_CATEGORY){
				if (sResult.contains("{")){
					if (progressDialog != null){
						progressDialog.setMessage("Updating...");
					}
					AnalysisData.analyze(sResult);
					if (AnalysisData.value){
						ArrayList<QuestionLite> questions = AnalysisData.
								mListQuestionLite;
						if (questions != null){
							if (questions.size()>0){
								DatabaseHelper data = new DatabaseHelper(this);
								data.openDataBase();
								data.insertQuestion(questions);
								data.close();
								Toast.makeText(this, 
										"update success!", Toast.LENGTH_LONG).show();
							}
						}
					}
					else{
						Toast.makeText(this, AnalysisData.message, Toast.LENGTH_LONG).show();
					}
					if (progressDialog != null){
						progressDialog.dismiss();
					}
				}
			}
		}
	}
	
}
