package com.example.tomohiko_sato.owltube.util;

/**
 * releaseではログを出力しない
 * TODO: crashlytics等への通知
 */
public class Logger {
	public static void d() {
		d("");
	}

	public static void d(String str, Object... args) {
	}

	public static void i() {
		i("");
	}

	public static void i(String str, Object... args) {
	}

	public static void w() {
		w("");
	}

	public static void w(String str, Object... args) {
	}

	public static void e() {
		e("");
	}

	public static void e(String str, Object... args) {
	}
}
