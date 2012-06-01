package com.ppclink.iqarena.ultil;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {

	public static String md5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			// Create Hex String
			StringBuffer hexString = new StringBuffer();
			for (int i = 0; i < messageDigest.length; i++)
				hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
			return hexString.toString();

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static boolean validateEmail(String emailstring) {
		Pattern emailPattern = Pattern.compile("[A-z0-9\\.]+@.+\\.[a-z]+");
		Matcher emailMatcher = emailPattern.matcher(emailstring);
		return emailMatcher.matches();
	}

	public static boolean validateString(String input) {
		Pattern inputPattern = Pattern.compile("[A-z0-9\\.]+");
		Matcher inputMatcher = inputPattern.matcher(input);
		return inputMatcher.matches();
	}
}
