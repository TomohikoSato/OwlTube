package com.example.tomohiko_sato.mytube.di;

import android.content.Context;

import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class SampleModule {
	private final Context context;

	public SampleModule(Context context) {
		this.context = context;
	}

	@Provides
	Context provideContext () {
		return context;
	}

	@Provides
	PopularUseCase providePopularUseCase (YoutubeRequest youtubeRequest) {
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	YoutubeRequest provideYoutubeRequest (Retrofit retrofit) {
		return new YoutubeRequest(retrofit);
	}

	@Provides
	Retrofit provideYoutubeRetrofit () {
		return new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}
}
