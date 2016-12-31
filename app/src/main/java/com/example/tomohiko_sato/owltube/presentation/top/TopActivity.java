package com.example.tomohiko_sato.owltube.presentation.top;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
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

	private enum TAB {
		POPULAR(0, "Popular", R.drawable.main_tab_popular) {
			@Override
			public Fragment getFragment() {
				return PopularFragment.newInstance();
			}
		},
		RECENTLY_WATCHED(1, "Recently Watched", R.drawable.main_tab_recent) {
			@Override
			public Fragment getFragment() {
				return RecentlyWatchedFragment.newInstance();
			}
		};

		public final int position;
		public final String title;
		public final int icon;

		TAB(int position, String title, @DrawableRes int icon) {
			this.position = position;
			this.title = title;
			this.icon = icon;
		}

		public static TAB fromPosition(int position) {
			for (TAB value : values()) {
				if (value.position == position) {
					return value;
				}
			}
			throw new IllegalArgumentException();
		}

		public abstract Fragment getFragment();
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
		tabLayout.addOnTabSelectedListener(new ActionBarTitleChanger(this.getSupportActionBar()));

		Objects.requireNonNull(tabLayout.getTabAt(TAB.POPULAR.position)).setTag(TAB.POPULAR.title).setIcon(TAB.POPULAR.icon).select();
		Objects.requireNonNull(tabLayout.getTabAt(TAB.RECENTLY_WATCHED.position)).setTag(TAB.RECENTLY_WATCHED.title).setIcon(TAB.RECENTLY_WATCHED.icon);
	}

	static class ActionBarTitleChanger implements TabLayout.OnTabSelectedListener {
		@NonNull
		private final ActionBar bar;

		ActionBarTitleChanger(@NonNull ActionBar bar) {
			this.bar = Objects.requireNonNull(bar);
		}

		@Override
		public void onTabSelected(TabLayout.Tab tab) {
			bar.setTitle((String) tab.getTag());
		}

		@Override
		public void onTabUnselected(TabLayout.Tab tab) {
		}

		@Override
		public void onTabReselected(TabLayout.Tab tab) {
			bar.setTitle((String) tab.getTag());
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
			return TAB.fromPosition(position).getFragment();
		}

		@Override
		public int getCount() {
			return TAB.values().length;
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
