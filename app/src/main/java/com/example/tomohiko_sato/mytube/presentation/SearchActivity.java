package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.annotation.VisibleForTesting;
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
import com.example.tomohiko_sato.mytube.api.google.GoogleRequest;
import com.example.tomohiko_sato.mytube.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Item;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;
import com.example.tomohiko_sato.mytube.api.youtube.data.videolist.VideoList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchActivity extends AppCompatActivity {
	private static final String TAG = SearchActivity.class.getSimpleName();

	private SearchResultListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_search);
		Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayShowTitleEnabled(false);

		ListView listView = (ListView) findViewById(R.id.list_view);
		adapter = new SearchResultListAdapter(this);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				PlayerActivity.startPlayerActivity(SearchActivity.this, adapter.getItem(position).id);
			}
		});
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		SearchView searchView = (SearchView) findViewById(R.id.search_search_view);
		searchView.setIconifiedByDefault(false);
		searchView.setFocusable(true);
		searchView.setQueryHint("Search Music");
		searchView.requestFocusFromTouch();

		final String[] from = new String[]{"cityName"};
		final int[] to = new int[]{android.R.id.text1};

		final SimpleCursorAdapter simpleCursorAdapter = new SimpleCursorAdapter(this,
				android.R.layout.simple_list_item_1,
				null,
				from,
				to,
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);

		searchView.setSuggestionsAdapter(simpleCursorAdapter);

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(final String query) {
				final YoutubeRequest youtubeRequest = new YoutubeRequest();
				youtubeRequest.searchAsync(query, new Callback<Search>() {
					@Override
					public void onResponse(Call<Search> call, Response<Search> response) {
						Log.d(TAG, "Search onResponse");
						final List<Item> items = response.body().items;

						final List<SearchResultViewModel> searchResultViewModels = new ArrayList<>();
						for (Item item : items) {
							searchResultViewModels.add(new SearchResultViewModel(item.id.videoId, item.snippet.title, item.snippet.channelTitle, item.snippet.thumbnails.medium.url));
						}

						adapter.setViewModels(searchResultViewModels);

						final StringBuilder stringBuilder = new StringBuilder();
						final String separator = ",";
						for (Item item : items) {
							stringBuilder.append(item.id.videoId);
							if (!items.equals(items.get(items.size() - 1))) {
								stringBuilder.append(separator);
							}
						}

						final String ids = stringBuilder.toString();
						youtubeRequest.videoListAsync(ids, new Callback<VideoList>() {
							@Override
							public void onResponse(Call<VideoList> call, Response<VideoList> response) {
								Log.d(TAG, "VideoList onResponse");
								for (int i = 0; i < response.body().items.size(); i++) {
									String viewCount = response.body().items.get(i).statistics.viewCount;
									searchResultViewModels.get(i).setViewCount(viewCount);
								}

								adapter.setViewModels(searchResultViewModels);
							}

							@Override
							public void onFailure(Call<VideoList> call, Throwable t) {
								Log.e(TAG, "VideoList onFailure " + t);
								Toast.makeText(SearchActivity.this, "再生回数の取得に失敗しました", Toast.LENGTH_LONG).show();
							}
						});

					}

					@Override
					public void onFailure(Call<Search> call, Throwable t) {
						Log.e(TAG, "Search onFailure " + t);
						Toast.makeText(SearchActivity.this, "検索結果の取得に失敗しました", Toast.LENGTH_LONG).show();
					}
				});

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				Log.d(TAG, "query text change" + newText);
				populateAdapter(newText, simpleCursorAdapter);
				return false;
			}
		});
		return true;
	}


	private static final String[] SUGGESTIONS = {
			"Bauru", "Sao Paulo", "Rio de Janeiro",
			"Bahia", "Mato Grosso", "Minas Gerais",
			"Tocantins", "Rio Grande do Sul"
	};

	private final GoogleRequest googleRequest = new GoogleRequest();
	private final Callback<List<List<String>>> suggestKeywordCallback = new Callback<List<List<String>>>() {
		@Override
		public void onResponse(Call<List<List<String>>> call, Response<List<List<String>>> response) {
			Log.d(TAG, response.raw().toString());
		}

		@Override
		public void onFailure(Call<List<List<String>>> call, Throwable t) {
			t.printStackTrace();
		}
	};

	// You must implements your logic to get data using OrmLite
	private void populateAdapter(String query, CursorAdapter hogeadapter) {
		googleRequest.fetchSuggestKeywordForYoutube(query, suggestKeywordCallback);


		final MatrixCursor c = new MatrixCursor(new String[]{BaseColumns._ID, "cityName"});
		for (int i = 0; i < SUGGESTIONS.length; i++) {
			if (SUGGESTIONS[i].toLowerCase().startsWith(query.toLowerCase()))
				c.addRow(new Object[]{i, SUGGESTIONS[i]});
		}
		hogeadapter.changeCursor(c);
	}

	@VisibleForTesting
	public static class SearchResultViewModel {
		final String id;
		final String title;
		final String channelTitle;
		String viewCount;
		final String thumbnailUrl;

		public SearchResultViewModel(String id, String title, String channelTitle, String thumbnailUrl) {
			this.id = id;
			this.title = title;
			this.channelTitle = channelTitle;
			this.thumbnailUrl = thumbnailUrl;
		}

		void setViewCount(String viewCount) {
			this.viewCount = convertDisplayViewCount(viewCount);
		}

		@VisibleForTesting
		public String convertDisplayViewCount(String viewCount) {
			if (viewCount == null || viewCount.equals("")) {
				return "0";
			}

			int intViewCount = Integer.parseInt(viewCount);

			if (intViewCount >= 10000) {
				return String.format(Locale.JAPAN, "%.1f万", intViewCount / 10000f);
			} else if (intViewCount >= 1000) {
				return String.format(Locale.JAPAN, "%d千", intViewCount / 1000);
			} else if (intViewCount >= 100) {
				return String.format(Locale.JAPAN, "%d", intViewCount / 100 * 100);
			} else if (intViewCount >= 10) {
				return String.format(Locale.JAPAN, "%d", intViewCount / 10 * 10);
			}

			return viewCount;
		}
	}

	static class SearchResultListAdapter extends ArrayAdapter<SearchResultViewModel> {
		private List<SearchResultViewModel> viewModels = new ArrayList<>();
		private final Context context;
		private final LayoutInflater inflater;

		public SearchResultListAdapter(Context context) {
			super(context, R.layout.search_result_list_item);
			this.context = context;
			this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		public void setViewModels(List<SearchResultViewModel> viewModels) {
			this.viewModels = viewModels;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return viewModels.size();
		}

		@Override
		public SearchResultViewModel getItem(int position) {
			return viewModels.get(position);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.search_result_list_item, parent, false);
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

			SearchResultViewModel viewModel = viewModels.get(position);
			holder.title.setText(viewModel.title);
			holder.viewCount.setText(viewModel.viewCount);
			holder.channelTitle.setText(viewModel.channelTitle);
			Picasso.with(context).load(viewModel.thumbnailUrl).into(holder.thumbnail);

			return convertView;
		}

		static class ViewHolder {
			TextView title;
			TextView channelTitle;
			TextView viewCount;
			ImageView thumbnail;

			ViewHolder(TextView title, TextView channelTitle, TextView viewCount, ImageView thumbnail) {
				this.title = title;
				this.channelTitle = channelTitle;
				this.viewCount = viewCount;
				this.thumbnail = thumbnail;
			}
		}
	}
}
