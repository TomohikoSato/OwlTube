package com.example.tomohiko_sato.mytube;

import android.app.Application;

import com.facebook.stetho.Stetho;


public class OwlTubeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
	}
}
