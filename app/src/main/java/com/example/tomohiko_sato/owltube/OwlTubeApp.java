package com.example.tomohiko_sato.owltube;

import android.app.Application;

import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;


public class OwlTubeApp extends Application {

	@Override
	public void onCreate() {
		super.onCreate();
		Stetho.initializeWithDefaults(this);
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);
	}
}
