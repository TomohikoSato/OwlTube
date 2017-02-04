package com.example.tomohiko_sato.owltube.util;

import android.util.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.example.tomohiko_sato.owltube.util.Logger.TagGenerater.getCallerClassName;

public class Logger {
	public static void d(String str, Object... args) {
		Log.d(getCallerClassName(), String.format(str, args));
	}

	public static void i(String str, Object... args) {
		Log.i(getCallerClassName(), String.format(str, args));
	}

	public static void w(String str, Object... args) {
		Log.w(getCallerClassName(), String.format(str, args));
	}

	public static void e(String str, Object... args) {
		Log.e(getCallerClassName(), String.format(str, args));
	}

	static class TagGenerater {
		private static final int CALL_STACK_INDEX = 2;
		private static final Pattern ANONYMOUS_CLASS = Pattern.compile("(\\$\\d+)+$");
		private static final int MAX_TAG_LENGTH = 23;

		/**
		 * 呼び出し元のクラス名を取得する。
		 */
		static String getCallerClassName() {
			StackTraceElement[] stackTrace = new Throwable().getStackTrace();
			if (stackTrace.length <= CALL_STACK_INDEX) {
				throw new IllegalStateException(
						"Synthetic stacktrace didn't have enough elements: are you using proguard?");
			}

			return createCallerClassName(stackTrace[CALL_STACK_INDEX]);
		}

		/**
		 * 呼び出し元のクラス名を取得する。
		 * 匿名クラスの場合、$1といったsuffixは除去される。
		 */
		private static String createCallerClassName(StackTraceElement element) {
			String callerClassName = element.getClassName();
			Matcher m = ANONYMOUS_CLASS.matcher(callerClassName);
			if (m.find()) {
				callerClassName = m.replaceAll("");
			}
			callerClassName = callerClassName.substring(callerClassName.lastIndexOf('.') + 1);
			return callerClassName.length() > MAX_TAG_LENGTH ? callerClassName.substring(0, MAX_TAG_LENGTH) : callerClassName;
		}
	}
}
