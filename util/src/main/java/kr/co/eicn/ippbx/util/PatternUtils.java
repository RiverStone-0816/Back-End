package kr.co.eicn.ippbx.util;

import java.util.regex.Pattern;

/**
 * @author tinywind
 * @since 2017-11-30
 */
public class PatternUtils {
	public static boolean isEncKey(String key) { return Pattern.matches("^[a-zA-Z0-9]*$", key); }

	public static boolean isEmail(String string) {
		return Pattern.matches("^[_a-z0-9-]+(.[_a-z0-9-]+)*@(?:\\w+\\.)+\\w+$", string);
	}

	public static boolean isIp(final String ip) {
		return Pattern.matches("\\b\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\b", ip);
	}

	public static boolean isDate(final String date) {
		return Pattern.matches("^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9]$", date);
	}

	public static boolean isDateTime(final String date) {
		return Pattern.matches("^[0-9][0-9][0-9][0-9]\\-[0-9][0-9]\\-[0-9][0-9] [0-9][0-9]\\:[0-9][0-9]\\:[0-9][0-9]$", date);
	}

	public static boolean isPhoneNumber(final String phoneNumber) {
		return Pattern.matches("^01(?:0|1|[6-9])(\\d{3,4})(\\d{4})$", phoneNumber);
	}

	public static boolean isPatternId(final String password) {
		return Pattern.matches("^([a-z0-9+]*$)", password);
	}

	public static boolean isPasswordValid(final String password) {
		return Pattern.matches("^(?=.*[A-Za-z])(?=.*\\d)(?=.*[$@$!%*#?&])[A-Za-z\\d$@$!%*#?&]{9,20}$", password);
	}

	public static boolean isPasswordSameCharacter(final String password) {
		return Pattern.compile("(.)\\1\\1").matcher(password).find();
	}

	public static boolean isHangule(final String text) {
		return Pattern.compile(".*[ㄱ-ㅎㅏ-ㅣ가-힣]+.*").matcher(text).find();
	}

	// \ / : * ? " |
	public static boolean isValidFileName(final String fileName) {
		return Pattern.compile("^[^.\\\\\\\\/:*?\\\"<>|]?[^\\\\\\\\/:*?\\\"<>|]*").matcher(fileName).find();
	}

	public static boolean isPasswordContinuousCharacter(final String password) {
		int count = 0;
		char c = 0;
		char before = 0;
		for (int i = 0; i < password.length(); i++) {
			c = password.charAt(i);
			if ((before + 1 == c)) {
				count++;
				if (count >= 2) return true;
			} else {
				count = 0;
			}
			before = c;
		}
		return false;
	}
}
