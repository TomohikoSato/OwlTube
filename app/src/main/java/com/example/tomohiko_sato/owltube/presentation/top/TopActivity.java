package com.example.tomohiko_sato.owltube.presentation.top;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TabLayout.Tab;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.setting.SettingActivity;
import com.example.tomohiko_sato.owltube.presentation.top.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.top.recently_watched.RecentlyWatchedFragment;

import java.util.Objects;

/**
 * 起動後最初に表示される、トップ画面を担うAcitivity.
 */
public class TopActivity extends AppCompatActivity implements VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener {
	private final static String TAG = TopActivity.class.getSimpleName();

	private enum TopTab {
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
		final int title;
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

		public static TopTab from(Tab tab) {
			return from(tab.getPosition());
		}

		public abstract Fragment getFragment();

		static void initialize(TabLayout layout) {
			for (TopTab value : values()) {
				Tab tab = Objects.requireNonNull(layout.getTabAt(value.position));
				tab.setIcon(value.icon);
				if (value == TopTab.POPULAR) {
					tab.select();
				}
			}
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addOnTabSelectedListener(new ActionBarTitleChanger(this.getSupportActionBar(), this));

		TopTab.initialize(tabLayout);
	}

	static class ActionBarTitleChanger implements TabLayout.OnTabSelectedListener {
		@NonNull
		private final ActionBar bar;

		@NonNull
		private final Context context;

		ActionBarTitleChanger(@NonNull ActionBar bar, @NonNull Context context) {
			this.bar = Objects.requireNonNull(bar);
			this.context = context;
		}

		@Override
		public void onTabSelected(Tab tab) {
			bar.setTitle(context.getString(TopTab.from(tab).title));
		}

		@Override
		public void onTabUnselected(Tab tab) {
		}

		@Override
		public void onTabReselected(Tab tab) {
			bar.setTitle(context.getString(TopTab.from(tab).title));
		}
	}

	@Override
	public void onVideoItemSelected(Video item) {
		PlayerActivity.startPlayerActivity(this, item);
	}

	static class SectionPagerAdapter extends FragmentPagerAdapter {
		SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return TopTab.from(position).getFragment();
		}

		@Override
		public int getCount() {
			return TopTab.values().length;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.menu_main_search:
				SearchActivity.startSearchActivity(this);
				return true;
			case R.id.menu_main_setting:
				SettingActivity.startSettingActivity(this);
				return true;
		}
		return false;
	}
}