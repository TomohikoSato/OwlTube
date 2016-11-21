package com.example.tomohiko_sato.mytube.presentation.search;

import android.content.Context;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.mytube.di.SampleModule;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.mytube.domain.util.Callback;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.presentation.player.PlayerActivity;
import com.example.tomohiko_sato.mytube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchActivity extends AppCompatActivity {
	private static final String TAG = SearchActivity.class.getSimpleName();

	private SearchResultListAdapter adapter;

	@Inject
	SearchUseCase searchUC;

	@Inject
	YoutubeRequest youtubeRequest;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		DaggerSampleComponent.builder().sampleModule(new SampleModule(this)).build().inject(this);

		ListView listView = (ListView) findViewById(R.id.list_view);
		adapter = new SearchResultListAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PlayerActivity.startPlayerActivity(SearchActivity.this, adapter.getItem(position));
			}
		});
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
						adapter.setViewModels(items);
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

	static class SearchResultListAdapter extends ArrayAdapter<VideoItem> {
		private List<VideoItem> viewModels = new ArrayList<>();
		private final Context context;
		private final LayoutInflater inflater;

		public SearchResultListAdapter(Context context) {
			super(context, R.layout.video_list_item);
			this.context = context;
			this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		public void setViewModels(List<VideoItem> viewModels) {
			this.viewModels = viewModels;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return viewModels.size();
		}

		@Override
		public VideoItem getItem(int position) {
			return viewModels.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.video_list_item, parent, false);
			}

			ViewHolder holder;
			if (convertView.getTag() == null) {
				TextView title = (TextView) convertView.findViewById(R.id.title);
				TextView channelTitle = (TextView) convertView.findViewById(R.id.channel_title);
				TextView createdAt = (TextView) convertView.findViewById(R.id.view_count);
				ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
				holder = new ViewHolder(title, channelTitle, createdAt, thumbnail);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			VideoItem viewModel = viewModels.get(position);
			holder.title.setText(viewModel.title);
			holder.viewCount.setText(StringUtil.convertDisplayViewCount(viewModel.viewCount));
			holder.channelTitle.setText(viewModel.channelTitle);
			Picasso.with(context).load(viewModel.thumbnailUrl).into(holder.thumbnail);

			return convertView;
		}

		static class ViewHolder {
			final TextView title;
			final TextView channelTitle;
			final TextView viewCount;
			final ImageView thumbnail;

			ViewHolder(TextView title, TextView channelTitle, TextView viewCount, ImageView thumbnail) {
				this.title = title;
				this.channelTitle = channelTitle;
				this.viewCount = viewCount;
				this.thumbnail = thumbnail;
			}
		}
	}
}
