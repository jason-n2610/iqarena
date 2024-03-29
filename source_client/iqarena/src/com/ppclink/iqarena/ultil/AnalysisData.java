/**
 * 
 */
package com.ppclink.iqarena.ultil;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ppclink.iqarena.object.Category;
import com.ppclink.iqarena.object.MemberScore;
import com.ppclink.iqarena.object.Question;
import com.ppclink.iqarena.object.QuestionLite;
import com.ppclink.iqarena.object.Rank;
import com.ppclink.iqarena.object.Room;
import com.ppclink.iqarena.object.User;

/**
 * @author hoangnh
 * 
 */

public class AnalysisData {

	public static String message = null;
	public static ArrayList<MemberScore> mListMemberInRoom = 
			new ArrayList<MemberScore>();
	public static ArrayList<MemberScore> mListMembersScore = 
			new ArrayList<MemberScore>();
	public static ArrayList<QuestionLite> mListQuestionLite = 
			new ArrayList<QuestionLite>();
	public static ArrayList<Room> mListRoom = new ArrayList<Room>();
	public static String mTrueAnswer = null;
	public static Question question = null;
	public static QuestionLite questionLite = null;
	public static int roomId = 0, memberId = 0;
	private static final String TAG = "JSONDATA";
	public static User userInfo = null;
	public static float updateScore = 0;
	public static int help_5050_remove1=0, help_5050_remove2=0; 
	public static ArrayList<Rank> mRanks = new ArrayList<Rank>();
	public static int mRankId = 0;
	public static ArrayList<Category> mListCategory = new ArrayList<Category>();

	public static boolean value = false;

	public static boolean analyze(String input) {
		boolean result = false;
		try {
			JSONObject jObject = new JSONObject(input);
			String typeMessage = jObject.getString("type");

			// message 'login'
			if (typeMessage.equals(Config.REQUEST_LOGIN)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop login thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data
								.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);
					}

				}
			}

			// message 'register'
			else if (typeMessage.equals(Config.REQUEST_REGISTER)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					// truong hop register thanh cong
					// lay ve thong tin user
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						int userId = json_data.getInt("user_id");
						String username = json_data.getString("username");
						String email = json_data.getString("email");
						float scoreLevel = json_data.getInt("score_level");
						String registedDate = json_data
								.getString("registed_date");
						int powerUser = json_data.getInt("power_user");
						float money = json_data.getInt("money");
						userInfo = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);
					}

				}
			}

			// message 'create_new_room'
			else if (typeMessage.equals(Config.REQUEST_CREATE_NEW_ROOM)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				roomId = jObject.getInt("room_id");
				memberId = jObject.getInt("member_id");
			}

			// message 'get_list_room'
			else if (typeMessage.equals(Config.REQUEST_GET_LIST_ROOM)) {
				value = jObject.getBoolean("value");
				// truong hop co room tra ve
				if (value) {
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					mListRoom.clear();
					for (int i = 0; i < length; i++) {
						Room room;
						JSONObject json_data = jArray.getJSONObject(i);
						int roomId = json_data.getInt("room_id");
						String roomName = json_data.getString("room_name");
						int ownerId = json_data.getInt("owner_id");
						String ownerName = json_data.getString("username");
						int maxMember = json_data.getInt("max_member");
						int betScore = json_data.getInt("bet_score");
						int timePerQuestion = json_data
								.getInt("time_per_question");
						room = new Room(roomId, roomName, ownerId, ownerName,
								maxMember, betScore, timePerQuestion);
						mListRoom.add(room);
					}
				} else {
					mListRoom.clear();
					message = jObject.getString("message");
				}
			}

			// message join room
			else if (typeMessage.equals(Config.REQUEST_JOIN_ROOM)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value){
					memberId = jObject.getInt("member_id");
				}
			}

			// message exit room
			else if (typeMessage.equals(Config.REQUEST_EXIT_ROOM)) {

			}

			// get members in room
			else if (typeMessage.equals(Config.REQUEST_GET_MEMBERS_IN_ROOM)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String jMembers = jObject.getString("members");
					JSONArray jArray = new JSONArray(jMembers);
					int length = jArray.length();
					mListMemberInRoom.clear();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						String memberId = json_data.getString("room_member_id");
						String userId = json_data.getString("user_id");
						String username = json_data.getString("username");
						String memberType = json_data.getString("member_type");

						MemberScore member = new MemberScore(memberId, 
								userId, username, "",
								"", "", "", memberType);

						mListMemberInRoom.add(member);
					}
				} else {
					mListMemberInRoom.clear();
				}
			}

			else if (typeMessage.equals(Config.REQUEST_GET_QUESTION)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String strId = null, strQuestion = null, 
							strA = null, strB = null, strC = null, strD = null;
					String jQuestion = jObject.getString("question");
					JSONArray jArray = new JSONArray(jQuestion);
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						strId = json_data.getString("question_id");
						strQuestion = json_data.getString("question_name");
						strA = json_data.getString("answer_a");
						strB = json_data.getString("answer_b");
						strC = json_data.getString("answer_c");
						strD = json_data.getString("answer_d");
					}
					question = new Question(strId, strQuestion, strA, strB,
							strC, strD);
				}
			} else if (typeMessage.equals(Config.REQUEST_GET_MEMBERS_ANSWER)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String jMembers = jObject.getString("answers");
					JSONArray jArray = new JSONArray(jMembers);
					int length = jArray.length();
					mListMembersScore.clear();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						
						String memberId = json_data.getString("room_member_id");
						String userId = json_data.getString("user_id");
						String username = json_data.getString("username");
						String lastAnswer = json_data.getString("last_answer");
						if (lastAnswer.equals("null") || lastAnswer.equals("0")) {
							lastAnswer = "-";
						} else if (lastAnswer.equals("1")) {
							lastAnswer = "A";
						} else if (lastAnswer.equals("2")) {
							lastAnswer = "B";
						} else if (lastAnswer.equals("3")) {
							lastAnswer = "C";
						} else if (lastAnswer.equals("4")) {
							lastAnswer = "D";
						}
						String score = json_data.getString("score");
						if (score.equals("null")) {
							score = "-";
						}
						String graft = json_data.getString("graft_id");
						if (graft.equals("null")) {
							graft = "-";
						}
						String combo = json_data.getString("combo");
						if (combo.equals("null")) {
							combo = "-";
						}
						
						String memberType = json_data.getString("member_type");

						MemberScore memberScore = new MemberScore(memberId, userId,
								username, lastAnswer, score, 
								graft, combo, memberType);

						mListMembersScore.add(memberScore);
					}

					// lay ra cau tra loi dung cho cau truoc va cau hoi tiep
					// theo neu co
					mTrueAnswer = jObject.getString("answer");
					question = null;
					if (jObject.has("next_question")) {
						String strId = null, strQuestion = null, 
								strA = null, strB = null, strC = null, strD = null;
						String jQuestion = jObject.getString("next_question");
						JSONArray jArray1 = new JSONArray(jQuestion);
						int len = jArray1.length();
						for (int i = 0; i < len; i++) {
							JSONObject json_data = jArray1.getJSONObject(i);
							strId = json_data.getString("question_id");
							strQuestion = json_data.getString("question_name");
							strA = json_data.getString("answer_a");
							strB = json_data.getString("answer_b");
							strC = json_data.getString("answer_c");
							strD = json_data.getString("answer_d");
						}
						question = new Question(strId, strQuestion, strA, strB,
								strC, strD);
					}
					
					// update score cho user khi nguoi choi ket thuc tro choi
					if (jObject.has("score")){
						updateScore = jObject.getInt("score");
						AnalysisData.userInfo.setScoreLevel(updateScore);
					}
				}
			}
			
			// request help 5050
			else if (typeMessage.equals(Config.REQUEST_HELP_5050)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					help_5050_remove1 = jObject.getInt("remove1");
					help_5050_remove2 = jObject.getInt("remove2");
				}
			}
			
			// request get question by type
			else if (typeMessage.equals(Config.REQUEST_GET_QUESTION_BY_TYPE)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String strId = null, strQuestion = null, 
							strA = null, strB = null, strC = null, strD = null;
					int answer = 0;
					String jQuestion = jObject.getString("question");
					JSONArray jArray = new JSONArray(jQuestion);
					int length = jArray.length();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						strId = json_data.getString("question_id");
						strQuestion = json_data.getString("question_name");
						strA = json_data.getString("answer_a");
						strB = json_data.getString("answer_b");
						strC = json_data.getString("answer_c");
						strD = json_data.getString("answer_d");
						answer = json_data.getInt("answer");
					}
					questionLite = new QuestionLite(Integer.valueOf(strId), strQuestion, 1, strA, strB,
							strC, strD, answer, "");
				}
			}
			else if (typeMessage.equals(Config.REQUEST_GET_TOP_RECORD)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String jMembers = jObject.getString("awards");
					JSONArray jArray = new JSONArray(jMembers);
					int length = jArray.length();
					mRanks.clear();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);
						int award_id = json_data.getInt("award_id");
						String user_name = json_data.getString("user_name");
						int score = json_data.getInt("score");
						String date_record = json_data.getString("date_record");

						Rank rank = new Rank(award_id, user_name, score, date_record);

						mRanks.add(rank);
					}
				} else {
					mListMemberInRoom.clear();
				}
			}
			else if (typeMessage.equals(Config.REQUEST_SUBMIT_RECORD)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value){
					mRankId = jObject.getInt("award_id");
				}
			}
			else if (typeMessage.equals(Config.REQUEST_UPLOAD_QUESTION)){
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
			}

			// message 'get_category'
			else if (typeMessage.equals(Config.REQUEST_GET_CATEGORY)) {
				value = jObject.getBoolean("value");
				// truong hop co room tra ve
				if (value) {
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					mListCategory.clear();
					for (int i = 0; i < length; i++) {
						Category category;
						JSONObject json_data = jArray.getJSONObject(i);
						int category_id = json_data.getInt("category_id");
						String category_name = json_data.getString("category_name");
						String date_create = json_data.getString("date_create");
						String describle = json_data.getString("describle_category");
						
						category = new Category(String.valueOf(category_id), 
								category_name, date_create, describle);
						mListCategory.add(category);
					}
				} else {
					mListCategory.clear();
					message = jObject.getString("message");
				}
			}
			else if (typeMessage.equals(Config.REQUEST_DOWNLOAD_CATEGORY)){
				value = jObject.getBoolean("value");
				// truong hop co room tra ve
				if (value) {
					String jInfo = jObject.getString("questions");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					mListQuestionLite.clear();
					for (int i = 0; i < length; i++) {
						QuestionLite question;
						JSONObject json_data = jArray.getJSONObject(i);
						int quesId, quesType, answer;
						String questionName, answerA, answerB, answerC, answerD;
						String descrbileAnswer;
						
						quesId= json_data.getInt("question_id");
						questionName = json_data.getString("question_name");
						quesType = json_data.getInt("question_type_id");
						answerA = json_data.getString("answer_a");
						answerB = json_data.getString("answer_b");
						answerC = json_data.getString("answer_c");
						answerD = json_data.getString("answer_d");
						answer = json_data.getInt("answer");
						descrbileAnswer = json_data.getString("describle_answer");
						
						question = new QuestionLite(quesId, questionName, 
								quesType, answerA, answerB, answerC, 
								answerD, answer, descrbileAnswer);
						mListQuestionLite.add(question);
					}
				} else {
					mListCategory.clear();
					message = jObject.getString("message");
				}
			}

			result = true;
		} catch (JSONException e) {
			Log.i(TAG, e.getMessage());
			result = false;
		}
		return result;
	}
}
