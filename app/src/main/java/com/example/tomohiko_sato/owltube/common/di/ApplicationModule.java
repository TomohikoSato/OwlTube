package com.example.tomohiko_sato.owltube.common.di;

import android.content.Context;

import com.example.tomohiko_sato.owltube.common.rx.RxBus;
import com.example.tomohiko_sato.owltube.domain.common.PermissionHandler;
import com.example.tomohiko_sato.owltube.domain.player.PlayerNotifier;
import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.owltube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleAPI;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeAPI;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDao;
import com.example.tomohiko_sato.owltube.infra.db.DefaultDBHelper;
import com.example.tomohiko_sato.owltube.util.StethoWrapper;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
	private final Context context;

	public ApplicationModule(Context applicationContext) {
		this.context = applicationContext;
	}

	@Provides
	@Reusable
	Context provideContext() {
		return context;
	}

	@Provides
	@Reusable
	PopularUseCase providePopularUseCase(YoutubeRequest youtubeRequest) {
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	@Reusable
	PlayerNotifier providePlayerNotifier(Context context) {
		return new PlayerNotifier(context);
	}


	@Provides
	@Reusable
	RecentlyWatchedUseCase provideRecentlyWatchedUseCase(RecentlyWatchedDao recentlyWatchedDao) {
		return new RecentlyWatchedUseCase(recentlyWatchedDao);
	}

	@Provides
	@Reusable
	PlayerUseCase providePlayerUseCase(RecentlyWatchedDao dao, YoutubeRequest request) {
		return new PlayerUseCase(dao, request);
	}


	@Provides
	@Reusable
	SearchUseCase provideSearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest, SearchHistoryDao dao) {
		return new SearchUseCase(youtubeRequest, googleRequest, dao);
	}

	@Provides
	@Reusable
	YoutubeRequest provideYoutubeRequest(YoutubeAPI api) {
		return new YoutubeRequest(api);
	}

	@Provides
	@Reusable
	YoutubeAPI provideYoutubeAPI() {
		return new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.addCallAdapterFactory(RxJava2CallAdapterFactory.create())
				.client(StethoWrapper.addStethoInterCeptor(new OkHttpClient.Builder()).build())
				.build().create(YoutubeAPI.class);
	}

	@Provides
	@Reusable
	GoogleRequest provideGoogleRequest() {
		return new GoogleRequest();
	}

	@Provides
	@Reusable
	GoogleAPI provideGoogleAPI() {
		return new Retrofit.Builder()
				.baseUrl("http://suggestqueries.google.com/complete/")
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(GoogleAPI.class);
	}

	@Provides
	@Reusable
	RecentlyWatchedDao provideRecentlyWatchedDao(DefaultDBHelper helper) {
		return new RecentlyWatchedDao(helper);
	}

	@Provides
	@Reusable
	DefaultDBHelper provideDefaultDBHelper() {
		return new DefaultDBHelper(context);
	}

	@Provides
	@Reusable
	SearchHistoryDao provideSearcHistoryDao(DefaultDBHelper helper) {
		return new SearchHistoryDao(helper);
	}

	@Provides
	@Singleton
	RxBus provideRxBus() {
		return new RxBus();
	}

	@Provides
	@Reusable
	PermissionHandler providePermissionHandler(Context context) {
		return new PermissionHandler(context);
	}
}
