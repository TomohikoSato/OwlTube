package com.example.tomohiko_sato.mytube.presentation;

import android.app.SearchManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

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
		SearchView searchView = (SearchView) findViewById(R.id.search_search_view);

		SearchManager searchManager =
				(SearchManager) getSystemService(Context.SEARCH_SERVICE);
		searchView.setSearchableInfo(
				searchManager.getSearchableInfo(getComponentName()));
		searchView.setIconifiedByDefault(false);
		searchView.setFocusable(true);
		searchView.setQueryHint("Search Music");
		searchView.requestFocusFromTouch();

		return true;
	}
}
