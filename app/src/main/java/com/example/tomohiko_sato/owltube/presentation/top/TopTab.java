package com.example.tomohiko_sato.owltube.presentation.top;


import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.presentation.top.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.top.recently_watched.RecentlyWatchedFragment;

import java.util.Objects;

import lombok.Getter;

enum TopTab {
	POPULAR(0, R.string.popular, R.drawable.main_tab_popular) {
		@Override
		public Fragment getFragment() {
			return PopularFragment.newInstance();
		}
	},
	RECENTLY_WATCHED(1, R.string.recently_watched, R.drawable.main_tab_recent) {
		@Override
		public Fragment getFragment() {
			return RecentlyWatchedFragment.newInstance();
		}
	};

	private final int position;

	@StringRes
	@Getter
	private final int title;

	@DrawableRes
	private final int icon;

	TopTab(int position, @StringRes int title, @DrawableRes int icon) {
		this.position = position;
		this.title = title;
		this.icon = icon;
	}

	public static TopTab from(int position) {
		for (TopTab value : values()) {
			if (value.position == position) {
				return value;
			}
		}
		throw new IllegalArgumentException();
	}

	public static TopTab from(TabLayout.Tab tab) {
		return from(tab.getPosition());
	}

	public abstract Fragment getFragment();

	static void initialize(TabLayout layout) {
		for (TopTab value : values()) {
			TabLayout.Tab tab = Objects.requireNonNull(layout.getTabAt(value.position));
			tab.setIcon(value.icon);
			if (value == TopTab.POPULAR) {
				tab.select();
			}
		}
	}
}
