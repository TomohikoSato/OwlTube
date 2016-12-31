package com.example.tomohiko_sato.owltube.presentation.top;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;

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
	private final static int TAB_INDEX_POPULAR = 0;
	private final static int TAB_INDEX_RECENTLY_WATCHED = 1;
	private final static String TAB_TITLE_POPULAR_TITLE = "Popular";
	private final static String TAB_TITLE_RECENTLY_WATCHED = "Recently Watched";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addOnTabSelectedListener(new TitleChangeListener(this));

		Objects.requireNonNull(tabLayout.getTabAt(TAB_INDEX_POPULAR)).setTag(TAB_TITLE_POPULAR_TITLE).setIcon(R.drawable.main_tab_top).select();
		Objects.requireNonNull(tabLayout.getTabAt(TAB_INDEX_RECENTLY_WATCHED)).setTag(TAB_TITLE_RECENTLY_WATCHED).setIcon(R.drawable.main_tab_recent);
	}

	static class TitleChangeListener implements TabLayout.OnTabSelectedListener {
		@NonNull
		private final ActionBar bar;

		TitleChangeListener(@NonNull AppCompatActivity activity) {
			bar = Objects.requireNonNull(activity.getSupportActionBar());
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
			Log.d(TAG, "position: " + position);

			switch (position) {
				case TAB_INDEX_POPULAR:
					return PopularFragment.newInstance();
				case TAB_INDEX_RECENTLY_WATCHED:
					return RecentlyWatchedFragment.newInstance();
				default:
					throw new IllegalArgumentException("Illegal position: " + position);
			}
		}

		@Override
		public int getCount() {
			return 2;
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
