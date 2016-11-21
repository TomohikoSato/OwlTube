package com.example.tomohiko_sato.mytube.presentation.search;


import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.mytube.di.SampleModule;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.mytube.domain.util.Callback;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.mytube.presentation.search.SearchHistoryFragment.OnSearchHistoryFragmentInteractionListener;
import com.example.tomohiko_sato.mytube.presentation.search.SearchResultFragment.OnSearchResultFragmentInteractionListener;

import java.util.List;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity implements OnSearchResultFragmentInteractionListener, OnSearchHistoryFragmentInteractionListener {
	private static final String TAG = SearchActivity.class.getSimpleName();

	@Inject
	SearchUseCase searchUC;

	@Inject
	YoutubeRequest youtubeRequest;

	private SearchResultFragment searchResultFragment;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		DaggerSampleComponent.builder().sampleModule(new SampleModule(this)).build().inject(this);

		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		searchResultFragment = new SearchResultFragment();
		transaction.add(R.id.fragment_container, searchResultFragment);
		transaction.commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		final SearchView searchView = (SearchView) findViewById(R.id.search_search_view);
		searchView.setIconifiedByDefault(false);
		searchView.setFocusable(true);
		searchView.setQueryHint("Search Music");
		searchView.requestFocusFromTouch();

		final String[] from = new String[]{"suggest"};
		final int[] to = new int[]{android.R.id.text1};

		final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1,
				null,
				from,
				to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		searchView.setSuggestionsAdapter(simpleCursorAdapter);
		searchView.setOnSuggestionListener(new SearchView.OnSuggestionListener() {
			@Override
			public boolean onSuggestionSelect(int position) {
				Log.d(TAG, "onSuggestionSelect");
				return false;
			}

			@Override
			public boolean onSuggestionClick(int position) {
				Log.d(TAG, "onSuggestionClick");
				MatrixCursor cursor = (MatrixCursor) simpleCursorAdapter.getItem(position);
				String query = cursor.getString(1);
				searchView.setQuery(query, true);

				return false;
			}
		});

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(final String query) {

				searchUC.search(query, new Callback<List<VideoItem>>() {
					@Override
					public void onSuccess(List<VideoItem> items) {
						Log.d(TAG, "Search onSuccess");
						searchResultFragment.setVideoItems(items);

					}

					@Override
					public void onFailure(Throwable t) {
						Log.e(TAG, "Search onFailure " + t);
						Toast.makeText(SearchActivity.this, "検索結果の取得に失敗しました", Toast.LENGTH_LONG).show();
					}
				});

				return false;
			}

			@Override
			public boolean onQueryTextChange(final String newText) {
				Log.d(TAG, "query text change" + newText);
				searchUC.fetchSuggest(newText, new Callback<List<String>>() {
					@Override
					public void onSuccess(List<String> suggests) {
						Log.d(TAG, "Search onSuccess");
						populateAdapter(newText, suggests, simpleCursorAdapter);
					}

					@Override
					public void onFailure(Throwable t) {
						Log.e(TAG, "onFailure " + t);
					}

				});
				return false;
			}
		});
		return true;
	}

	private void populateAdapter(String query, List<String> suggests, CursorAdapter adapter) {
		final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "suggest"});
		for (int i = 0; i < suggests.size(); i++) {
			if (suggests.get(i).toLowerCase().startsWith(query.toLowerCase()))
				c.addRow(new Object[]{i, suggests.get(i)});
		}
		adapter.changeCursor(c);
	}

	@Override
	public void OnSearchHistoryFragmentInteraction(String searchHistory) {
		searchUC.search(searchHistory, new Callback<List<VideoItem>>() {
			@Override
			public void onSuccess(List<VideoItem> items) {
				Log.d(TAG, "Search onSuccess");
				searchResultFragment.setVideoItems(items);
			}

			@Override
			public void onFailure(Throwable t) {
				Log.e(TAG, "Search onFailure " + t);
				Toast.makeText(SearchActivity.this, "検索結果の取得に失敗しました", Toast.LENGTH_LONG).show();
			}
		});
	}

	@Override
	public void onSearchResultFragmentInteraction(VideoItem item) {
		PlayerActivity.startPlayerActivity(this, item);
	}
}
