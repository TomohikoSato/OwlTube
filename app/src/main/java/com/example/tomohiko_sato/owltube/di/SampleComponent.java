package com.example.tomohiko_sato.owltube.di;

import com.example.tomohiko_sato.owltube.presentation.main.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.main.recently_watched.RecentlyWatchedFragment;
import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchHistoryFragment;

import dagger.Component;

@Component(modules = SampleModule.class)
public interface SampleComponent {
	void inject(PopularFragment fragment);

	void inject(RecentlyWatchedFragment fragment);

	void inject(SearchActivity activity);

	void inject(PlayerActivity activity);

	void inject(SearchHistoryFragment fragment);
}
