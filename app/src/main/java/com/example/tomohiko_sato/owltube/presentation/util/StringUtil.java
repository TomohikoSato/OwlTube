package com.example.tomohiko_sato.owltube.presentation.util;

import java.util.Locale;

public class StringUtil {

	/**
	 * 正確な視聴回数から表示用に丸めた視聴回数へ変換する
	 */
	public static String convertDisplayViewCount(String viewCount) {
		if (viewCount == null || viewCount.equals("")) {
			return "0";
		}

		int intViewCount = Integer.parseInt(viewCount);

		if (intViewCount >= 10000) {
			return String.format(Locale.JAPAN, "%.1f万", intViewCount / 10000f);
		} else if (intViewCount >= 1000) {
			return String.format(Locale.JAPAN, "%d千", intViewCount / 1000);
		} else if (intViewCount >= 100) {
			return String.format(Locale.JAPAN, "%d", intViewCount / 100 * 100);
		} else if (intViewCount >= 10) {
			return String.format(Locale.JAPAN, "%d", intViewCount / 10 * 10);
		}

		return viewCount;
	}


}
