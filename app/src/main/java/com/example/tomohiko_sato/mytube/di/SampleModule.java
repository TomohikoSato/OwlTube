package com.example.tomohiko_sato.mytube.di;

import android.content.Context;

import com.example.tomohiko_sato.mytube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.mytube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.mytube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.mytube.infra.api.google.GoogleAPI;
import com.example.tomohiko_sato.mytube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeAPI;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.dao.RecentlyWatchedDao;
import com.example.tomohiko_sato.mytube.infra.dao.RecentlyWatchedDaoImpl;
import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

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
	Context provideContext() {
		return context;
	}

	@Provides
	PopularUseCase providePopularUseCase(YoutubeRequest youtubeRequest) {
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	RecentlyWatchedUseCase provideRecentlyWatchedUseCase(YoutubeRequest youtubeRequest, Context context, RecentlyWatchedDao recentlyWatchedDao) {
		return new RecentlyWatchedUseCase(youtubeRequest, context, recentlyWatchedDao);
	}

	@Provides
	PlayerUseCase providePlayerUseCase(RecentlyWatchedDao dao) {
		return new PlayerUseCase(dao);
	}

	@Provides
	SearchUseCase provideSearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest) {
		return new SearchUseCase(youtubeRequest, googleRequest);
	}

	@Provides
	YoutubeRequest provideYoutubeRequest(YoutubeAPI api) {
		return new YoutubeRequest(api);
	}

	@Provides
	YoutubeAPI provideYoutubeAPI() {
		return new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(YoutubeAPI.class);
	}

	@Provides
	GoogleRequest provideGoogleRequest() {
		return new GoogleRequest();
	}

	@Provides
	GoogleAPI provideGoogleAPI(Retrofit retrofit) {
		return new Retrofit.Builder()
				.baseUrl("http://suggestqueries.google.com/complete/")
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(GoogleAPI.class);
	}

	@Provides
	RecentlyWatchedDao provideRecentlyWatchedDao(DefaultDBHelper helper) {
		return new RecentlyWatchedDaoImpl(helper);
	}

	@Provides
	DefaultDBHelper provideDefaultDBHelper(Context contxt) {
		return new DefaultDBHelper(context);
	}
}
