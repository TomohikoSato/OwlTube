package com.example.tomohiko_sato.owltube.di;

import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchHistoryFragment;
import com.example.tomohiko_sato.owltube.presentation.top.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.top.recently_watched.RecentlyWatchedFragment;

import dagger.Subcomponent;

@Subcomponent(modules = PresentationModule.class)
public interface PresentationComponent {
    void inject(SearchActivity activity);

    void inject(PlayerActivity activity);

    void inject(PopularFragment fragment);

    void inject(RecentlyWatchedFragment fragment);

    void inject(SearchHistoryFragment fragment);

}
