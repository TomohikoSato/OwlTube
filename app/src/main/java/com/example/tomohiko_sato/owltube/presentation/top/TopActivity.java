package com.example.tomohiko_sato.owltube.presentation.top;

import android.os.Bundle;
import android.support.design.widget.BottomNavigationView;
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

/**
 * 起動後最初に表示される、トップ画面を担うAcitivity.
 */
public class TopActivity extends AppCompatActivity implements VideoItemViewAdapter.OnVideoItemSelectedListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_top);
		setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));

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
}