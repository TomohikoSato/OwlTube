package com.example.tomohiko_sato.owltube;

import android.app.Application;

import com.example.tomohiko_sato.owltube.di.AppComponent;
import com.example.tomohiko_sato.owltube.di.AppModule;
import com.example.tomohiko_sato.owltube.di.DaggerAppComponent;
import com.facebook.stetho.Stetho;
import com.squareup.leakcanary.LeakCanary;


public class OwlTubeApp extends Application {
	private AppComponent appComponent;

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

		appComponent = DaggerAppComponent.builder().appModule(new AppModule()).build();
	}
}
