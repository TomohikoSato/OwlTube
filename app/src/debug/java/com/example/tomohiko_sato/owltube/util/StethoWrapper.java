package com.example.tomohiko_sato.owltube.util;

import android.content.Context;

import com.facebook.stetho.Stetho;

public class StethoWrapper {
	public static void setUp(Context context) {
		Stetho.initializeWithDefaults(context);
	}
}
