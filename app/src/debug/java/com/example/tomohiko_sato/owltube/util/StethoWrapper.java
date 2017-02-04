package com.example.tomohiko_sato.owltube.util;

import android.content.Context;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import okhttp3.OkHttpClient;

public class StethoWrapper {
	public static void setUp(Context context) {
		Stetho.initializeWithDefaults(context);
	}

	public static OkHttpClient.Builder addStethoInterCeptor(OkHttpClient.Builder builder) {
		return builder.addNetworkInterceptor(new StethoInterceptor());
	}
}
