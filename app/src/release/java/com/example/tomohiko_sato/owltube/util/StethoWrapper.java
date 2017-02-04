package com.example.tomohiko_sato.owltube.util;

import android.content.Context;

/**
 * debugビルドでのみStethoを利用するためのWrapper
 */
public class StethoWrapper {
	public static void setUp(Context context) {
		/** no op */
	}

	public static OkHttpClient.Builder addStethoInterCeptor(OkHttpClient.Builder builder) {
		return builder;
	}
}
