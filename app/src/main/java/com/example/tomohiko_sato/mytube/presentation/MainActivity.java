package com.example.tomohiko_sato.mytube.presentation;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.presentation.dummy.DummyContent;

import java.util.zip.Inflater;

/**
 * 起動後最初に表示されるメインの画面
 */
public class MainActivity extends AppCompatActivity implements TopFragment.OnTopFragmentInteractionListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);

		LayoutInflater inflater = LayoutInflater.from(this);

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

		TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
		/*
		View tabItem = inflater.inflate(R.layout.tab_item, null);
		tabLayout.addTab(tabLayout.newTab().setCustomView(tabItem));
		tabLayout.addTab(tabLayout.newTab().setCustomView(tabItem));
		*/

		tabLayout.setupWithViewPager(viewPager);

		tabLayout.getTabAt(0).setText("Top").setIcon(R.drawable.top);
		tabLayout.getTabAt(1).setText("Recent").setIcon(R.drawable.recent);
	}

	@Override
	public void onTopFragmentInteraction(DummyContent.DummyItem item) {

	}

	static class SectionPagerAdapter extends FragmentPagerAdapter {

		public SectionPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return TopFragment.newInstance(1);
		}

		@Override
		public int getCount() {
			return 2;
		}

/*		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
				case 0:
					return "hoge";
				case 1:
					return "fuga";
			}

			return null;
		}*/
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
		}
		return false;
	}
}
