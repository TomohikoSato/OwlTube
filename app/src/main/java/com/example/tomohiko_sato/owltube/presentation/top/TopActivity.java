package com.example.tomohiko_sato.owltube.presentation.top;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.owltube.presentation.search.SearchActivity;
import com.example.tomohiko_sato.owltube.presentation.setting.SettingFragment;
import com.example.tomohiko_sato.owltube.presentation.top.popular.PopularFragment;
import com.example.tomohiko_sato.owltube.presentation.top.recently_watched.RecentlyWatchedFragment;

/**
 * 起動後最初に表示される、トップ画面を担うAcitivity.
 */
public class TopActivity extends AppCompatActivity implements VideoItemViewAdapter.OnVideoItemSelectedListener {

	private Fragment popularFragment;
	private Fragment recentlyWatchedFragment;
	private Fragment settingFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		Toolbar toolBar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolBar);

		initFragments(savedInstanceState);
		BottomNavigationView bottomNavi = (BottomNavigationView) findViewById(R.id.bottom_nav);
		bottomNavi.setOnNavigationItemSelectedListener((item) -> {
			toolBar.setTitle(item.getTitle());
			item.setChecked(true);
			switch (item.getItemId()) {
				case R.id.menu_popular:
					switchFragment(popularFragment, PopularFragment.TAG);
					break;
				case R.id.menu_recently_watched:
					switchFragment(recentlyWatchedFragment, RecentlyWatchedFragment.TAG);
					break;
				case R.id.menu_setting:
					switchFragment(settingFragment, SettingFragment.TAG);
					break;
			}
			return false;
		});
		setSupportActionBar(toolBar);
		bottomNavi.setSelectedItemId(R.id.menu_popular);
	}

	@Override
	protected void onResume() {
		super.onResume();
		Snackbar.make(findViewById(android.R.id.content), "hogehoge", Snackbar.LENGTH_SHORT).show();
	}

	private void initFragments(Bundle savedInstanceState) {
		final FragmentManager manager = getSupportFragmentManager();
		popularFragment = manager.findFragmentByTag(PopularFragment.TAG);
		recentlyWatchedFragment = manager.findFragmentByTag(RecentlyWatchedFragment.TAG);
		settingFragment = manager.findFragmentByTag(SettingFragment.TAG);

		if (popularFragment == null) {
			popularFragment = PopularFragment.newInstance();
		}
		if (recentlyWatchedFragment == null) {
			recentlyWatchedFragment = RecentlyWatchedFragment.newInstance();
		}
		if (settingFragment == null) {
			settingFragment = SettingFragment.newInstance();
		}

		if (savedInstanceState == null) {
			switchFragment(popularFragment, PopularFragment.class.getSimpleName());
		}
	}


	private boolean switchFragment(@NonNull Fragment fragment, @NonNull String tag) {
		if (fragment.isAdded()) {
			return false;
		}

		final FragmentManager manager = getSupportFragmentManager();
		final FragmentTransaction ft = manager.beginTransaction();

		final Fragment currentFragment = manager.findFragmentById(R.id.content_view);
		if (currentFragment != null) {
			ft.detach(currentFragment);
		}
		if (fragment.isDetached()) {
			ft.attach(fragment);
		} else {
			ft.add(R.id.content_view, fragment, tag);
		}
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
				.commit();

		// NOTE: When this method is called by user's continuous hitting at the same time,
		// transactions are queued, so necessary to reflect commit instantly before next transaction starts.
		manager.executePendingTransactions();

		return true;
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
		}
		return false;
	}
}