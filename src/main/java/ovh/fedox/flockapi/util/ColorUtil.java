package ovh.fedox.flockapi.util;

import lombok.experimental.UtilityClass;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ColorUtil.java -
 * <p>
 * Created on 3/30/2025 at 1:12 AM by Fedox.
 * Copyright © 2025 Fedox. All rights reserved.
 */

@UtilityClass
public class ColorUtil {

	private static final Pattern GRADIENT_PATTERN = Pattern.compile("<gradient=#([0-9A-Fa-f]{6}),#([0-9A-Fa-f]{6})>(.*?)</gradient>");
	private static final Pattern COLOR_PATTERN = Pattern.compile("<color=#([0-9A-Fa-f]{6})>(.*?)</color>");
	private static final Pattern BOLD_PATTERN = Pattern.compile("<bold>(.*?)</bold>");

	/**
	 * Process text with color and formatting tags
	 *
	 * @param text Text with tags like <color=#hexColor>text</color>, <gradient=#startColor,#endColor>text</gradient>, <bold>text</bold>
	 * @return Formatted text with Minecraft color codes
	 */
	public static String format(String text) {
		text = processBoldTags(text);

		text = processColorTags(text);

		text = processGradientTags(text);

		return text;
	}

	/**
	 * Process bold tags in text
	 */
	private static String processBoldTags(String text) {
		Matcher boldMatcher = BOLD_PATTERN.matcher(text);
		StringBuffer result = new StringBuffer();

		while (boldMatcher.find()) {
			String content = boldMatcher.group(1);
			String boldText = "§l" + content + "§r";
			boldMatcher.appendReplacement(result, Matcher.quoteReplacement(boldText));
		}
		boldMatcher.appendTail(result);

		return result.toString();
	}

	/**
	 * Process color tags in text
	 */
	private static String processColorTags(String text) {
		Matcher colorMatcher = COLOR_PATTERN.matcher(text);
		StringBuffer result = new StringBuffer();

		while (colorMatcher.find()) {
			String color = colorMatcher.group(1);
			String content = colorMatcher.group(2);

			// Apply color to the content
			String coloredText = colorize(content, color) + "§r";
			colorMatcher.appendReplacement(result, Matcher.quoteReplacement(coloredText));
		}
		colorMatcher.appendTail(result);

		return result.toString();
	}

	/**
	 * Process gradient tags in text
	 */
	private static String processGradientTags(String text) {
		Matcher gradientMatcher = GRADIENT_PATTERN.matcher(text);
		StringBuffer result = new StringBuffer();

		while (gradientMatcher.find()) {
			String startColor = gradientMatcher.group(1);
			String endColor = gradientMatcher.group(2);
			String content = gradientMatcher.group(3);

			// Check for bold tags within the gradient content
			boolean hasBold = content.contains("§l");

			// Apply gradient to the content, preserving bold formatting
			String gradientText = applyGradientWithFormatting(content, startColor, endColor) + "§r";
			gradientMatcher.appendReplacement(result, Matcher.quoteReplacement(gradientText));
		}
		gradientMatcher.appendTail(result);

		return result.toString();
	}

	/**
	 * Apply a single color to text
	 *
	 * @param text     Text to colorize
	 * @param hexColor Hex color (with or without #)
	 * @return Colorized text
	 */
	public static String colorize(String text, String hexColor) {
		if (hexColor.startsWith("#")) {
			hexColor = hexColor.substring(1);
		}

		if (hexColor.length() != 6) {
			throw new IllegalArgumentException("Hex color needs to be exact 6 letters.");
		}

		StringBuilder formattedText = new StringBuilder();
		formattedText.append("§x");

		for (char c : hexColor.toCharArray()) {
			formattedText.append("§").append(c);
		}

		formattedText.append(text);

		return formattedText.toString();
	}

	/**
	 * Apply a gradient between two colors to text, preserving formatting codes
	 *
	 * @param text     Text to apply gradient to (may contain formatting codes)
	 * @param startHex Starting hex color (without #)
	 * @param endHex   Ending hex color (without #)
	 * @return Text with gradient applied
	 */
	private static String applyGradientWithFormatting(String text, String startHex, String endHex) {
		if (text.isEmpty()) {
			return "";
		}

		StringBuilder plainText = new StringBuilder();
		StringBuilder result = new StringBuilder();
		boolean isBold = false;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '§' && i + 1 < text.length()) {
				char formatCode = text.charAt(i + 1);
				if (formatCode == 'l') {
					isBold = true;
				} else if (formatCode == 'r') {
					isBold = false;
				}
				i++;
			} else {
				plainText.append(c);
			}
		}

		// Parse RGB values
		int startR = Integer.parseInt(startHex.substring(0, 2), 16);
		int startG = Integer.parseInt(startHex.substring(2, 4), 16);
		int startB = Integer.parseInt(startHex.substring(4, 6), 16);

		int endR = Integer.parseInt(endHex.substring(0, 2), 16);
		int endG = Integer.parseInt(endHex.substring(2, 4), 16);
		int endB = Integer.parseInt(endHex.substring(4, 6), 16);

		String plainString = plainText.toString();
		double stepR = plainString.length() > 1 ? (double) (endR - startR) / (plainString.length() - 1) : 0;
		double stepG = plainString.length() > 1 ? (double) (endG - startG) / (plainString.length() - 1) : 0;
		double stepB = plainString.length() > 1 ? (double) (endB - startB) / (plainString.length() - 1) : 0;

		isBold = false;
		int plainTextIndex = 0;

		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);

			if (c == '§' && i + 1 < text.length()) {
				char formatCode = text.charAt(i + 1);
				if (formatCode == 'l') {
					isBold = true;
				} else if (formatCode == 'r') {
					isBold = false;
				}
				i++;
			} else {
				int r = (int) Math.round(startR + (stepR * plainTextIndex));
				int g = (int) Math.round(startG + (stepG * plainTextIndex));
				int b = (int) Math.round(startB + (stepB * plainTextIndex));

				String hexColor = String.format("%02X%02X%02X", r, g, b);

				result.append("§x");
				for (char hc : hexColor.toCharArray()) {
					result.append("§").append(hc);
				}

				if (isBold) {
					result.append("§l");
				}

				result.append(c);
				plainTextIndex++;
			}
		}

		return result.toString();
	}
}

