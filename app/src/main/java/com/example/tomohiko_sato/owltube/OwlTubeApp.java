package com.example.tomohiko_sato.owltube;

import android.app.Application;

import com.example.tomohiko_sato.owltube.common.di.ApplicationComponent;
import com.example.tomohiko_sato.owltube.common.di.ApplicationModule;
import com.example.tomohiko_sato.owltube.common.di.DaggerApplicationComponent;
import com.example.tomohiko_sato.owltube.util.StethoWrapper;
import com.squareup.leakcanary.LeakCanary;


public class OwlTubeApp extends Application {

	private ApplicationComponent applicationComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		StethoWrapper.setUp(this);
		if (LeakCanary.isInAnalyzerProcess(this)) {
			// This process is dedicated to LeakCanary for heap analysis.
			// You should not init your app in this process.
			return;
		}
		LeakCanary.install(this);

		applicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(getApplicationContext())).build();
	}

	public ApplicationComponent getComponent() {
		return applicationComponent;
	}
}
