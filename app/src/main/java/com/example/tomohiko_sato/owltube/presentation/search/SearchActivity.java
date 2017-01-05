package com.example.tomohiko_sato.owltube.presentation.search;


import android.content.Context;
import android.content.Intent;
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
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener;
import com.example.tomohiko_sato.owltube.presentation.player.PlayerActivity;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends AppCompatActivity implements OnVideoItemSelectedListener, SearchHistoryFragment.OnSearchHistoryFragmentInteractionListener, SearchResultFragment.SearchResultFragmentInteractionListener {
	private static final String TAG = SearchActivity.class.getSimpleName();

	@Inject
	SearchUseCase searchUC;

	private SearchResultFragment searchResultFragment;
	private SearchHistoryFragment searchHistoryFragment;
	private SearchView searchView;
	private Fragment currentShowingFragment;
	private final CompositeDisposable disposables = new CompositeDisposable();

	public static void startSearchActivity(Context context) {
		context.startActivity(new Intent(context, SearchActivity.class));
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		setSupportActionBar((Toolbar) findViewById(R.id.tool_bar));
		Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

		((OwlTubeApp) getApplication()).getComponent().inject(this);

		searchHistoryFragment = SearchHistoryFragment.newInstance();
		searchResultFragment = SearchResultFragment.newInstance(null);

		showSearchHistoryFragment();
	}

	private void showSearchHistoryFragment() {
		if (currentShowingFragment == searchHistoryFragment) {
			return;
		}
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container, searchHistoryFragment);
		transaction.commit();
		currentShowingFragment = searchHistoryFragment;
	}

	private void showSearchResultFragment() {
		if (currentShowingFragment == searchResultFragment) {
			return;
		}
		FragmentManager manager = getSupportFragmentManager();
		FragmentTransaction transaction = manager.beginTransaction();
		transaction.replace(R.id.fragment_container, searchResultFragment);
		transaction.commit();
		currentShowingFragment = searchResultFragment;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		searchView = (SearchView) findViewById(R.id.search_search_view);
		searchView.setIconifiedByDefault(false);
		searchView.setFocusable(true);
		searchView.setQueryHint("Search Music");
		//TODO: 検索履歴画面の時だけ見せたい
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
				search(query);
				return false;
			}

			@Override
			public boolean onQueryTextChange(final String newText) {
				if (newText.length() == 0) {
					return false;
				}
				Log.d(TAG, "onQueryTextChange" + newText);
				showSearchHistoryFragment();

				disposables.add(searchUC.fetchSuggest(newText)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeWith(new SuggestsObserver(newText, simpleCursorAdapter)));
				return false;
			}
		});
		return true;
	}

	class SuggestsObserver extends DisposableObserver<List<String>> {
		private final String newText;
		private final SimpleCursorAdapter adapter;

		SuggestsObserver(String newText , SimpleCursorAdapter adapter) {
			this.newText = newText;
			this.adapter = adapter;
		}

		@Override
		public void onNext(List<String> suggests) {
			populateAdapter(newText, suggests, adapter);
		}

		@Override
		public void onError(Throwable t) {
			t.printStackTrace();
		}

		@Override
		public void onComplete() {

		}
	}

	private String lastQueriedWord;
	private String nextPageToken = null;

	private void search(String query) {
		lastQueriedWord = query;
		hideKeyboard();
		showSearchResultFragment();

		disposables.add(searchUC.search(query, nextPageToken).first
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(videoResponse -> {
					nextPageToken = videoResponse.pageToken;
					searchResultFragment.addVideoItems(videoResponse.videos);
				}, throwable -> {
					Log.e(TAG, "Search onFailure " + throwable);
					Toast.makeText(SearchActivity.this, "検索結果の取得に失敗しました", Toast.LENGTH_LONG).show();
				}));
	}

	@Override
	protected void onStop() {
		super.onStop();
		disposables.dispose();
	}

	private void hideKeyboard() {
		View view = this.getCurrentFocus();
		if (view != null) {
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
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
		searchView.setQuery(searchHistory, true);
	}

	@Override
	public void onLoadMore() {
		search(lastQueriedWord);
	}

	@Override
	public void onVideoItemSelected(Video item) {
		PlayerActivity.startPlayerActivity(this, item);
	}
}
