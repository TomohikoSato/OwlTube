package com.example.tomohiko_sato.mytube.di;

import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;

import dagger.Module;
import dagger.Provides;

@Module
public class SampleModule {

	@Provides
	PopularUseCase providePopularUseCase () {
		return new PopularUseCase();
	}
}
