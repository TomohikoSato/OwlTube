package com.example.tomohiko_sato.mytube.di;

import com.example.tomohiko_sato.mytube.domain.player.PlayerUseCase;
import com.example.tomohiko_sato.mytube.presentation.main.popular.PopularFragment;
import com.example.tomohiko_sato.mytube.presentation.main.recently_watched.RecentlyWatchedFragment;
import com.example.tomohiko_sato.mytube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.mytube.presentation.search.SearchActivity;

import dagger.Component;

@Component(modules = SampleModule.class)
public interface SampleComponent {
	void inject(PopularFragment fragment);
	void inject(RecentlyWatchedFragment fragment);
	void inject(SearchActivity activity);
	void inject(PlayerActivity activity);
}
