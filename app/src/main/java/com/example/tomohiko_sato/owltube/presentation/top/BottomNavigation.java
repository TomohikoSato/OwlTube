package com.example.tomohiko_sato.owltube.presentation.top;


import com.example.tomohiko_sato.owltube.R;

enum BottomNavigation {
	POPULAR(R.id.menu_popular),
	RECENTLY_WATCHED(R.id.menu_recently_watched),
	SETTING(R.id.menu_main_setting);

	

	private final int menuId;

	BottomNavigation(int menuId) {
		this.menuId = menuId;
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
