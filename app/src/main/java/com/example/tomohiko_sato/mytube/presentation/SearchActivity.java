package com.example.tomohiko_sato.mytube.presentation;

import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.tomohiko_sato.mytube.R;

public class SearchActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.search_tool_bar);

		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_search, menu);

		MenuItem item = menu.findItem(R.id.menu_search_search);
		SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);

		searchView.setIconifiedByDefault(false);

		return true;
	}


}
