package com.example.tomohiko_sato.mytube.di;

import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;

import dagger.Module;
import dagger.Provides;

@Module
public class SampleModule {

	@Provides
	PopularUseCase providePopularUseCase (YoutubeRequest youtubeRequest) {
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	YoutubeRequest provideYoutubeRequest () {
		return new YoutubeRequest();
	}
}
