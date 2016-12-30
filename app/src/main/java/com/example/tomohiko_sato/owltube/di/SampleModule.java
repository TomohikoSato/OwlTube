package com.example.tomohiko_sato.owltube.di;

import android.content.Context;
import android.util.Log;

import com.example.tomohiko_sato.owltube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.owltube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleAPI;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeAPI;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDao;
import com.example.tomohiko_sato.owltube.infra.dao.RecentlyWatchedDaoImpl;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDao;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDaoImpl;
import com.example.tomohiko_sato.owltube.infra.db.DefaultDBHelper;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class SampleModule {
	private final Context context;
	private final static String TAG = SampleModule.class.getSimpleName();


	public SampleModule(Context context) {
		this.context = context;
	}

	@Provides
	Context provideContext() {
		Log.d(TAG, "provicdeContext");
		return context;
	}

	@Provides
	PopularUseCase providePopularUseCase(YoutubeRequest youtubeRequest) {
		Log.d(TAG, "provicdePopularUseCase");
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	RecentlyWatchedUseCase provideRecentlyWatchedUseCase(YoutubeRequest youtubeRequest, Context context, RecentlyWatchedDao recentlyWatchedDao) {
		Log.d(TAG, "provicdeRecentlyWatchedUseCase");
		return new RecentlyWatchedUseCase(youtubeRequest, context, recentlyWatchedDao);
	}

	@Provides
	PlayerUseCase providePlayerUseCase(RecentlyWatchedDao dao, YoutubeRequest request) {
		Log.d(TAG, "provicdePlayerUseCase");
		return new PlayerUseCase(dao, request);
	}


	@Provides
	SearchUseCase provideSearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest, SearchHistoryDao dao) {
		Log.d(TAG, "provideSearchUseCase");
		return new SearchUseCase(youtubeRequest, googleRequest, dao);
	}

	@Provides
	YoutubeRequest provideYoutubeRequest(YoutubeAPI api) {
		Log.d(TAG, "provideYoutubeRequest");
		return new YoutubeRequest(api);
	}

	@Provides
	YoutubeAPI provideYoutubeAPI() {
		Log.d(TAG, "provideYoutubeAPI");
		return new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(YoutubeAPI.class);
	}

	@Provides
	GoogleRequest provideGoogleRequest() {
		Log.d(TAG, "provideGoogleRequest");
		return new GoogleRequest();
	}

	@Provides
	GoogleAPI provideGoogleAPI() {
		Log.d(TAG, "provideGoogleAPI");
		return new Retrofit.Builder()
				.baseUrl("http://suggestqueries.google.com/complete/")
				.addConverterFactory(GsonConverterFactory.create())
				.build().create(GoogleAPI.class);
	}

	@Provides
	RecentlyWatchedDao provideRecentlyWatchedDao(DefaultDBHelper helper) {
		Log.d(TAG, "provideRecentlyWatchedDao");
		return new RecentlyWatchedDaoImpl(helper);
	}

	@Provides
	DefaultDBHelper provideDefaultDBHelper() {
		Log.d(TAG, "provideDefaultDBHelper");
		return new DefaultDBHelper(context);
	}

	@Provides
	SearchHistoryDao provideSearcHistoryDao (DefaultDBHelper helper) {
		Log.d(TAG, "provideSearcHistoryDao");
		return new SearchHistoryDaoImpl(helper);
	}
}
