/**
 * 
 */
package com.ppclink.iqarena.ultil;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.ppclink.object.MemberScore;
import com.ppclink.object.Question;
import com.ppclink.object.Room;
import com.ppclink.object.User;

/**
 * @author hoangnh
 * 
 */

public class FilterResponse {

	public static String message = null;
	public static ArrayList<User> mListMemberInRoom = new ArrayList<User>();
	public static ArrayList<MemberScore> mListMembersScore = new ArrayList<MemberScore>();
	public static ArrayList<Room> mListRoom = new ArrayList<Room>();
	public static String mTrueAnswer = null;
	public static Question question = null;
	public static int roomId = 0, memberId = 0;
	private static final String TAG = "JSONDATA";
	public static User userInfo = null;

	public static boolean value = false;

	public static boolean filter(String input) {
		boolean result = false;
		try {
			Log.i(TAG, input);
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
					Log.i(TAG, "length: " + length);
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
					Log.i(TAG, "length: " + length);
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
				Log.i(TAG, "get list room");
				value = jObject.getBoolean("value");
				// truong hop co room tra ve
				if (value) {
					String jInfo = jObject.getString("info");
					JSONArray jArray = new JSONArray(jInfo);
					int length = jArray.length();
					Log.i(TAG, "length: " + length);
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
				memberId = jObject.getInt("member_id");
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
					Log.i(TAG, "length: " + length);
					mListMemberInRoom.clear();
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

						User user = new User(userId, username, email,
								scoreLevel, registedDate, powerUser, money);

						mListMemberInRoom.add(user);
					}
				} else {
					mListMemberInRoom.clear();
				}
			}

			else if (typeMessage.equals(Config.REQUEST_GET_QUESTION)) {
				value = jObject.getBoolean("value");
				message = jObject.getString("message");
				if (value) {
					String strId = null, strQuestion = null, strA = null, strB = null, strC = null, strD = null;
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
					Log.i(TAG, "length: " + length);
					mListMembersScore.clear();
					for (int i = 0; i < length; i++) {
						JSONObject json_data = jArray.getJSONObject(i);

						String userId = json_data.getString("user_id");
						String username = json_data.getString("username");
						String lastAnswer = json_data.getString("last_answer");
						if (lastAnswer.equals("null")) {
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

						MemberScore memberScore = new MemberScore(userId,
								username, lastAnswer, score, graft, combo);

						mListMembersScore.add(memberScore);
					}

					// lay ra cau tra loi dung cho cau truoc va cau hoi tiep
					// theo neu co

					mTrueAnswer = jObject.getString("answer");
					if (input.contains("next_question")) {
						String strId = null, strQuestion = null, strA = null, strB = null, strC = null, strD = null;
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
						question = null;
						question = new Question(strId, strQuestion, strA, strB,
								strC, strD);
					}
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
