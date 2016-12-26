package com.example.tomohiko_sato.owltube.di;

import com.example.tomohiko_sato.owltube.OwlTubeApp;

import dagger.Component;

@Component(modules = AppModule.class)
public interface AppComponent {
	void inject(OwlTubeApp app);
}
