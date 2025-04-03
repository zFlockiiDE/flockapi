package ovh.fedox.flockapi.util;


import lombok.experimental.UtilityClass;

/**
 * TextUtil.java - Text utilities
 * <p>
 * Created on 3/30/2025 at 3:32 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@UtilityClass
public class TextUtil {

	/**
	 * Attention: This method is a bit broken and can't reproduce more than 1 of the same letter and don't support upper case letters.
	 *
	 * @param text The text to convert
	 * @return The converted text
	 * @deprecated This method is deprecated and may not work as expected.
	 */
	@Deprecated
	public static String convertToTinyLetter(String text) {
		StringBuilder builder = new StringBuilder();

		for (char c : text.toCharArray()) {
			switch (c) {
				case 'a':
					builder.append("ᴀ");
					break;
				case 'b':
					builder.append("ʙ");
					break;
				case 'c':
					builder.append("ᴄ");
					break;
				case 'd':
					builder.append("ᴅ");
					break;
				case 'e':
					builder.append("ᴇ");
					break;
				case 'f':
					builder.append("ғ");
					break;
				case 'g':
					builder.append("ɢ");
					break;
				case 'h':
					builder.append("ʜ");
					break;
				case 'i':
					builder.append("ɪ");
					break;
				case 'j':
					builder.append("ᴊ");
					break;
				case 'k':
					builder.append("ᴋ");
					break;
				case 'l':
					builder.append("ʟ");
					break;
				case 'm':
					builder.append("ᴍ");
					break;
				case 'n':
					builder.append("ɴ");
					break;
				case 'o':
					builder.append("ᴏ");
					break;
				case 'p':
					builder.append("ᴘ");
					break;
				case 'q':
					builder.append("ǫ");
					break;
				case 'r':
					builder.append("ʀ");
					break;
				case 's':
					builder.append("s");
					break;
				case 't':
					builder.append("ᴛ");
					break;
				case 'u':
					builder.append("ᴜ");
					break;
				case 'v':
					builder.append("ᴠ");
					break;
				case 'w':
					builder.append("ᴡ");
					break;
				case 'x':
					builder.append("x");
					break;
				case 'y':
					builder.append("ʏ");
					break;
				case 'z':
					builder.append("ᴢ");
					break;
				default:
					builder.append(c);
					break;
			}
		}

		return builder.toString();
	}

}
