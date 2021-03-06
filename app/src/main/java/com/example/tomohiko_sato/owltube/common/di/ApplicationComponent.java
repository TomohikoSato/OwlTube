package com.example.tomohiko_sato.owltube.common.di;

import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.player.external.ExternalPlayerService;
import com.example.tomohiko_sato.owltube.presentation.player.external.PlayerNotificationReceiver;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchHistoryFragment;
import com.example.tomohiko_sato.owltube.presentation.top.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.top.recently_watched.RecentlyWatchedFragment;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
	void inject(PopularFragment fragment);

	void inject(RecentlyWatchedFragment fragment);

	void inject(SearchActivity activity);

	void inject(PlayerActivity activity);

	void inject(SearchHistoryFragment fragment);

	void inject(ExternalPlayerService service);

	void inject(PlayerNotificationReceiver playerNotificationReceiver);
}
