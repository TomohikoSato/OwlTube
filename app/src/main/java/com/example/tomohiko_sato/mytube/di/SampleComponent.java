package com.example.tomohiko_sato.mytube.di;

import com.example.tomohiko_sato.mytube.presentation.main.popular.PopularFragment;

import dagger.Component;

@Component(modules = SampleModule.class)
public interface SampleComponent {
	void inject(PopularFragment fragment);
}
