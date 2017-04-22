package com.example.tomohiko_sato.owltube.presentation.top;


import com.example.tomohiko_sato.owltube.R;

enum BottomNavigation {
	POPULAR(R.id.menu_popular, "popular"),
	RECENTLY_WATCHED(R.id.menu_recently_watched, "recently_watched"),
	SETTING(R.id.menu_setting, "setting");

	private final int menuId;
	private final String tag;

	BottomNavigation(int menuId, String tag) {
		this.menuId = menuId;
		this.tag = tag;
	}


	static BottomNavigation of(int menuId) {
		for (BottomNavigation value : values()) {
			if (value.menuId == menuId) {
				return value;
			}
		}
		throw new IllegalArgumentException("illegal menuId: " + menuId);
	}
}
