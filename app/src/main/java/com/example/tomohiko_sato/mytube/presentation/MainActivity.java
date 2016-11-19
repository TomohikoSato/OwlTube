package com.example.tomohiko_sato.mytube.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Item;
import com.example.tomohiko_sato.mytube.presentation.recentlywatched.RecentlyWatchedFragment;
import com.example.tomohiko_sato.mytube.presentation.recentlywatched.RecentlyWatchedRecyclerViewAdapter;
import com.example.tomohiko_sato.mytube.presentation.top.TopFragment;

/**
 * 起動後最初に表示されるメインの画面
 */
public class MainActivity extends AppCompatActivity implements TopFragment.OnTopFragmentInteractionListener, RecentlyWatchedFragment.OnRecentlyWatchedFragmentInteractionListener {
	private final static String TAG = MainActivity.class.getSimpleName();
	SectionPagerAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		adapter = new SectionPagerAdapter(getSupportFragmentManager());
		viewPager.setAdapter(adapter);
		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
			}

			@Override
			public void onPageSelected(int position) {
				if (position == 1) {
					RecentlyWatchedFragment fragment = ((RecentlyWatchedFragment) adapter.getRegisteredFragment(position));
					fragment.refreshItem(MainActivity.this);
				}
			}

			@Override
			public void onPageScrollStateChanged(int state) {
			}
		});

		final TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		tabLayout.setupWithViewPager(viewPager);

		tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab) {
				getSupportActionBar().setTitle((String) tab.getTag());
			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {
			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {
				getSupportActionBar().setTitle((String) tab.getTag());
			}
		});

		tabLayout.getTabAt(0).setTag("Popular").setIcon(R.drawable.main_tab_top).select();
		tabLayout.getTabAt(1).setTag("Recently Watched").setIcon(R.drawable.main_tab_recent);
	}

	@Override
	public void onTopFragmentInteraction(Item item) {
		PlayerActivity.startPlayerActivity(this, item.id);
	}

	@Override
	public void onRecentlyWatchedFragmentInteraction(String videoId) {
		PlayerActivity.startPlayerActivity(this, videoId);
	}

	static class SectionPagerAdapter extends FragmentPagerAdapter {
		SparseArray<Fragment> registeredFragments = new SparseArray<Fragment>();

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d(TAG, "position: " + position);

			switch (position) {
				case 0:
					return TopFragment.newInstance();
				case 1:
					return RecentlyWatchedFragment.newInstance();
				default:
					throw new IllegalArgumentException("Illegal position: " + position);
			}
		}

		@Override
		public int getCount() {
			return 2;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			Fragment fragment = (Fragment) super.instantiateItem(container, position);
			registeredFragments.put(position, fragment);
			return fragment;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			registeredFragments.remove(position);
			super.destroyItem(container, position, object);
		}

		public Fragment getRegisteredFragment(int position) {
			return registeredFragments.get(position);
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
				startActivity(new Intent(this, SearchActivity.class));
				return true;
			case R.id.menu_main_setting:
				startActivity(new Intent(this, SettingActivity.class));
				return true;
		}
		return false;
	}
}
