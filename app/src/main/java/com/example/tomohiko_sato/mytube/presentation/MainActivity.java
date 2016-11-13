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
import android.view.Menu;
import android.view.MenuItem;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.presentation.dummy.DummyContent;

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

		ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setAdapter(new SectionPagerAdapter(getSupportFragmentManager()));

		TabLayout tabLayout= (TabLayout) findViewById(R.id.tab_layout);

		tabLayout.addTab(tabLayout.newTab().setText("tab 1"));
		tabLayout.addTab(tabLayout.newTab().setText("tab 2"));

		tabLayout.setupWithViewPager(viewPager);
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

		@Override
		public CharSequence getPageTitle(int position) {

			switch (position) {
				case 0:
					return "Top";
				case 1:
					return "Recent";
			}

			return null;
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
		}
		return false;
	}
}
