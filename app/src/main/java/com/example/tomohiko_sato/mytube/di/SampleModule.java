package com.example.tomohiko_sato.mytube.di;

import android.content.Context;

import com.example.tomohiko_sato.mytube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.mytube.domain.recently_watched.RecentlyWatchedUseCase;
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
	Context provideContext () {
		return context;
	}

	@Provides
	PopularUseCase providePopularUseCase (YoutubeRequest youtubeRequest) {
		return new PopularUseCase(youtubeRequest);
	}

	@Provides
	RecentlyWatchedUseCase provideRecentlyWatchedUseCase (YoutubeRequest youtubeRequest, Context context, RecentlyWatchedDao recentlyWatchedDao) {
		return new RecentlyWatchedUseCase(youtubeRequest, context, recentlyWatchedDao);
	}

	@Provides
	PlayerUseCase providePlayerUseCase() {
		return new PlayerUseCase();
	}

	@Provides
	YoutubeRequest provideYoutubeRequest (YoutubeAPI api) {
		return new YoutubeRequest(api);
	}

	@Provides
	Retrofit provideYoutubeRetrofit () {
		return new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();
	}

	@Provides
	YoutubeAPI provideYoutubeAPI(Retrofit retrofit) {
		return retrofit.create(YoutubeAPI.class);
	}

	@Provides
	RecentlyWatchedDao provideRecentlyWatchedDao(DefaultDBHelper helper) {
		return new RecentlyWatchedDaoImpl(helper);
	}

	@Provides
	DefaultDBHelper provideDefaultDBHelper (Context contxt) {
		return new DefaultDBHelper(context);
	}
}
