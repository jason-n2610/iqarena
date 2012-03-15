package at.test.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Config {
//	public static final String REQUEST_SERVER_ADDR = "http://10.0.2.2/iqarena/source_server/index.php";
//	public static final String REQUEST_SERVER_ADDR = "http://iqarena.freevnn.com/";
	public static final String REQUEST_SERVER_ADDR = "http://192.168.1.14/iqarena/source_server/index.php";
	public static final String PATH_CONFIG = "/mnt/sdcard/iqarena/";
	public static final String NAME_CONFIG = "config.dat";
	public static final String REQUEST_LOGIN = "login";
	public static final String REQUEST_REGISTER = "register";
	public static final String REQUEST_GET_LIST_ROOM = "get_list_room";
	public static final String REQUEST_CREATE_NEW_ROOM = "create_new_room";
	public static final String REQUEST_REMOVE_ROOM = "remove_room";
	public static final String REQUEST_JOIN_ROOM = "join_room";
	public static final String REQUEST_EXIT_ROOM = "exit_room";
	public static final String REQUEST_GET_MEMBERS_IN_ROOM = "get_members_in_room";
	public static final String REQUEST_CHECK_CHANGE_ROOM = "check_change_room";
	public static final String REQUEST_CHECK_MEMBERS_IN_ROOM = "check_members_in_room";
	
//	private static String message;
//
//	public static String getMessage() {
//		return message;
//	}

//	/*
//	 * Ham save config cua app save duoi dang cac dong dong 1: username dong 2:
//	 * password
//	 */
//	public static boolean saveConfigFile(ArrayList<String> content) {
//		message = null;
//		boolean result = true;
//		String path = Config.PATH_CONFIG;
//		File rootDir = new File(path);
//		if (!(rootDir.exists() && rootDir.isDirectory())) {
//			try {
//				rootDir.mkdirs();
//				result = true;
//			} catch (SecurityException e) {
//				message = e.getMessage();
//				result = false;
//			}
//		}
//		if (result) {
//			File configFile = new File(rootDir, NAME_CONFIG);
//			try {
//				configFile.createNewFile();
//				BufferedWriter buffWriter = new BufferedWriter(new FileWriter(
//						configFile), 8 * 1024);
//				int size = content.size();
//				if (size > 0) {
//					for (int i = 0; i < size - 1; i++) {
//						buffWriter.write(content.get(i));
//						buffWriter.newLine();
//					}
//					buffWriter.write(content.get(size - 1));
//				}
//				buffWriter.flush();
//				buffWriter.close();
//			} catch (IOException e) {
//				result = false;
//				message = e.getMessage();
//				e.printStackTrace();
//			}
//		}
//
//		return result;
//	}
//	/*
//	 * save config file duoi dang nhieu String chuyen vao
//	 */
//	public static boolean saveConfigFile(String str1, String str2) {
//		message = null;
//		boolean result = true;
//		String path = Config.PATH_CONFIG;
//		File rootDir = new File(path);
//		if (!(rootDir.exists() && rootDir.isDirectory())) {
//			try {
//				rootDir.mkdirs();
//				result = true;
//			} catch (SecurityException e) {
//				message = e.getMessage();
//				result = false;
//			}
//		}
//		if (result) {
//			File configFile = new File(rootDir, NAME_CONFIG);
//			try {
//				configFile.createNewFile();
//				BufferedWriter buffWriter = new BufferedWriter(new FileWriter(
//						configFile), 8 * 1024);
//				buffWriter.write(str1);
//				buffWriter.newLine();
//				buffWriter.write(str2);
//				buffWriter.flush();
//				buffWriter.close();
//			} catch (IOException e) {
//				result = false;
//				message = e.getMessage();
//				e.printStackTrace();
//			}
//		}
//
//		return result;
//	}
//
//	/*
//	 * Ham tra ve list cac dong trong file config dong 1: username dong 2:
//	 * password
//	 */
//	public static ArrayList<String> getConfigFile() {
//		message = null;
//		ArrayList<String> result = null;
//		File configFile = new File(Config.PATH_CONFIG, NAME_CONFIG);
//		if (configFile.exists()) {
//			try {
//				BufferedReader buffReader = new BufferedReader(new FileReader(
//						configFile), 8 * 1024);
//				String line;
//				result = new ArrayList<String>();
//				while ((line = buffReader.readLine()) != null) {
//					result.add(line);
//				}
//			} catch (FileNotFoundException e) {
//				message = e.getMessage();
//				return null;
//			} catch (IOException e) {
//				message = e.getMessage();
//				return null;
//			}
//		}
//		return result;
//	}
//
//	public static boolean destroyConfigFile() {
//		boolean result = false;
//		message = null;
//		File configFile = new File(PATH_CONFIG, NAME_CONFIG);
//		if (configFile.exists()){
//			return configFile.delete();
//		}
//		return result;
//	}
}
