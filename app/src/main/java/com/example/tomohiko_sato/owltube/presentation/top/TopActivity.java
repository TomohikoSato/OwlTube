package com.example.tomohiko_sato.owltube.presentation.top;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
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
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.setting.SettingActivity;

import java.util.Objects;

/**
 * 起動後最初に表示される、トップ画面を担うAcitivity.
 */
public class TopActivity extends AppCompatActivity implements VideoItemViewAdapter.OnVideoItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);
		tabLayout.addOnTabSelectedListener(new TitleChanger(this.getSupportActionBar()));

		((BottomNavigationView) findViewById(R.id.bottom_nav)).setOnNavigationItemSelectedListener((item) -> {
			switch (item.getItemId()) {
				case R.id.menu_popular:
					break;
				case R.id.menu_recently_watched:
					break;
				case R.id.menu_setting:
					break;
			}
			return true;
		});

		TopTab.initialize(tabLayout);
	}

	@Override
	public void onVideoItemSelected(Video item) {
		PlayerActivity.startPlayerActivity(this, item);
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

	private static class TitleChanger implements TabLayout.OnTabSelectedListener {
		@NonNull
		private final ActionBar bar;

		TitleChanger(@NonNull ActionBar bar) {
			this.bar = Objects.requireNonNull(bar);
		}

		@Override
		public void onTabSelected(Tab tab) {
			bar.setTitle(TopTab.from(tab).getTitle());
		}

		@Override
		public void onTabUnselected(Tab tab) {
		}

		@Override
		public void onTabReselected(Tab tab) {
			bar.setTitle(TopTab.from(tab).getTitle());
		}
	}

	private static class SectionPagerAdapter extends FragmentPagerAdapter {
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
}