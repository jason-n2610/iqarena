package com.ppclink.iqarena.database;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ppclink.iqarena.object.Rank;
import com.ppclink.iqarena.object.QuestionLite;

public class DatabaseHelper extends SQLiteOpenHelper{

	//The Android's default system path of your application database.
    private static String DB_PATH;
 
    private static String DB_NAME = "db.sqlite";
 
    private SQLiteDatabase myDataBase; 
 
    private final Context mContext;
    private String tag = "DatabaseHelper";
	
	public DatabaseHelper(Context context) {
		super(context, DB_NAME, null, 1);
		mContext = context;
		DB_PATH =  mContext.getExternalFilesDir(null).toString();
		DB_PATH = DB_PATH + "/database/";
		File dir = new File(DB_PATH);
		if (!dir.exists()){
			dir.mkdir();
		}
	}
	
	 /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
 
    	SQLiteDatabase checkDB = null;
 
    	try{
    		String myPath = DB_PATH + DB_NAME;
    		File file = new File(myPath);
    		if (!file.exists()){
    			return false;
    		}
    		checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
 
    	}catch(SQLiteException e){
 
    		Log.e(tag, e.getMessage());
 
    	}
 
    	if(checkDB != null){
 
    		checkDB.close();
 
    	}
 
    	return checkDB != null ? true : false;
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException{
 
    	boolean dbExist = checkDataBase();
 
    	if(dbExist){
    		//do nothing - database already exist
    	}else{
 
    		//By calling this method and empty database will be created into the default system path
               //of your application so we are gonna be able to overwrite that database with our database.
        	this.getReadableDatabase();
 
        	try {
 
    			copyDataBase();
 
    		} catch (IOException e) {
 
        		throw new Error("Error copying database");
 
        	}
    	}
 
    }
    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
 
    	//Open your local db as the input stream
    	InputStream myInput = mContext.getAssets().open(DB_NAME);
 
    	// Path to the just created empty db
    	String outFileName = DB_PATH + DB_NAME;
 
    	//Open the empty db as the output stream
    	OutputStream myOutput = new FileOutputStream(outFileName);
 
    	//transfer bytes from the inputfile to the outputfile
    	byte[] buffer = new byte[1024];
    	int length;
    	while ((length = myInput.read(buffer))>0){
    		myOutput.write(buffer, 0, length);
    	}
 
    	//Close the streams
    	myOutput.flush();
    	myOutput.close();
    	myInput.close();
 
    }
    
    public void openDataBase() throws SQLException{
    	 
    	//Open the database
        String myPath = DB_PATH + DB_NAME;
    	myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS );
 
    }
    
    public QuestionLite getQuestion(int type){    	
		QuestionLite question= null;
		if (myDataBase != null){
			String query = 
						"SELECT _id, question_name, " +
						"question_type, answer_a, answer_b, answer_c, " +
						"answer_d, answer, describle_answer " +
						"FROM questions " +
						"WHERE question_type="+type+" "+
						"ORDER BY RANDOM() " +
						"LIMIT 1 ";
			Cursor c = myDataBase.rawQuery(query, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						int quesId = c.getInt(c.getColumnIndex("_id"));
						String quesName = c.getString(c.
								getColumnIndex("question_name"));
						int quesType = c.getInt(c.getColumnIndex("question_type"));
						String answerA = c.getString(c.getColumnIndex("answer_a"));
						String answerB = c.getString(c.getColumnIndex("answer_b"));
						String answerC = c.getString(c.getColumnIndex("answer_c"));
						String answerD = c.getString(c.getColumnIndex("answer_d"));
						int answer = c.getInt(c.getColumnIndex("answer"));
						String desAnswer = c.getString(c.
								getColumnIndex("describle_answer"));
						question = new QuestionLite(quesId, 
								quesName, quesType, answerA, answerB, answerC, 
								answerD, answer, desAnswer);
						
					} while (c.moveToNext());
				}
			}
			c.close();
		}
		return question;
	}
    
    public ArrayList<QuestionLite> getQuestion2(){   
    	ArrayList<QuestionLite> questions = null;
		if (myDataBase != null){
			questions = new ArrayList<QuestionLite>();
			String query = 
						"SELECT _id, question_name, " +
						"question_type, answer_a, answer_b, answer_c, " +
						"answer_d, answer, describle_answer " +
						"FROM questions " ;
			Cursor c = myDataBase.rawQuery(query, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						QuestionLite question= null;
						int quesId = c.getInt(c.getColumnIndex("_id"));
						String quesName = c.getString(c.
								getColumnIndex("question_name"));
						int quesType = c.getInt(c.getColumnIndex("question_type"));
						String answerA = c.getString(c.getColumnIndex("answer_a"));
						String answerB = c.getString(c.getColumnIndex("answer_b"));
						String answerC = c.getString(c.getColumnIndex("answer_c"));
						String answerD = c.getString(c.getColumnIndex("answer_d"));
						int answer = c.getInt(c.getColumnIndex("answer"));
						String desAnswer = c.getString(c.
								getColumnIndex("describle_answer"));
						question = new QuestionLite(quesId, 
								quesName, quesType, answerA, answerB, answerC, 
								answerD, answer, desAnswer);
						questions.add(question);
						
					} while (c.moveToNext());
				}
			}
			c.close();
		}
		return questions;
	}

	public ArrayList<QuestionLite> getQuestions(){
		ArrayList<QuestionLite> alQuestion = null;
		if (myDataBase != null){
			alQuestion = new ArrayList<QuestionLite>();
			for (int i=0; i<15; i++){
				QuestionLite question = getQuestion(i+1);
				if (question != null){
					alQuestion.add(question);
				}
			}
		}
		return alQuestion;
	}

	
	public boolean insertQuestion(String quesName, 
    		int quesType, String answerA, String answerB, 
    		String answerC,String answerD, String answer, String desAnswer){
    	boolean result = false;
    	if (myDataBase != null){
			String query = "INSERT INTO " + "questions" + "VALUES ('"
					+ quesName + "', " + quesType + ", " + "'" + answerA
					+ "', " + "'" + answerB + "', " + "'" + answerC + "', "
					+ "'" + answerD + "', " + answer + " ," + "'" + desAnswer
					+ "');";
			myDataBase.execSQL(query);
			result = true;
    	}
    	return result;
    }
    
    public boolean insertQuestion(ArrayList<QuestionLite> questions){
    	boolean result = false;
    	if (myDataBase != null){
    		String query1 = "INSERT INTO questions(question_name, question_type, " +
    				"answer_a, answer_b, answer_c, answer_d, answer) \n";
    		int size = questions.size();
    		for (int i=0; i<size; i++){
    			QuestionLite question = questions.get(i);
    			String query = query1 + "VALUES ('"+question.getQuesName()+"', " + question.getQuesType()+", "
    					+"'"+question.getAnswerA()+"', "+"'"+question.getAnswerB()+"', "
    					+"'"+question.getAnswerC()+"', "+"'"+question.getAnswerD()+"', "
    					+question.getAnswer() + ") \n";

        		myDataBase.execSQL(query.toString());
    		}
    	}
    	return result;
    }
    
    public ArrayList<Rank> getAwards(){    	
    	ArrayList<Rank> awards = null;
    	if (myDataBase != null){
			try{
				openDataBase();
			}
			catch (SQLException e) {
				Log.e(tag, e.getMessage());
			}
    		awards = new ArrayList<Rank>();
    		String query = "SELECT _id, name, score " +
    				"FROM awards "+
    				"ORDER BY score DESC " +
    				"LIMIT 20 ";
    		Cursor c = myDataBase.rawQuery(query, null);
			if (c != null) {
				if (c.moveToFirst()) {
					do {
						int _id = c.getInt(c.getColumnIndex("_id"));
						String name = c.getString(c.
								getColumnIndex("name"));
						int score = c.getInt(c.getColumnIndex("score"));
						Rank award = new Rank(_id, name, score);
						awards.add(award);
						
					} while (c.moveToNext());
				}
			}
			c.close();
    	}
    	close();
    	return awards;
    }
    
    public void insertAward(String name, int score){
    	if (myDataBase != null){
			try{
				openDataBase();
			}
			catch (SQLException e) {
				Log.e(tag, e.getMessage());
			}
			String query = "INSERT INTO " + "awards(name, score) " + "VALUES ('"
					+ name + "', " + score + "); ";
			myDataBase.execSQL(query);
    	}
    	close();
    }
    
    @Override
	public synchronized void close() {
 
    	    if(myDataBase != null)
    		    myDataBase.close();
 
    	    super.close();
 
	}
    
	@Override
	public void onCreate(SQLiteDatabase db) {
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
	}

}
