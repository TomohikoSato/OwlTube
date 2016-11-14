package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
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
import com.example.tomohiko_sato.mytube.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Item;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Snippet;
import com.example.tomohiko_sato.mytube.api.youtube.data.videolist.VideoList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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

		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(final String query) {
				final YoutubeRequest youtubeRequest = new YoutubeRequest();
				youtubeRequest.searchAsync(query, new Callback<Search>() {
					@Override
					public void onResponse(Call<Search> call, Response<Search> response) {
						final List<Item> items = response.body().items;

						final List<SearchResultViewModel> searchResultViewModels = new ArrayList<>();
						for (Item item : items) {
							searchResultViewModels.add(new SearchResultViewModel(item.id.videoId, item.snippet.title, item.snippet.channelTitle, null, item.snippet.thumbnails.medium.url));
						}

						adapter.setViewModels(searchResultViewModels);

						final StringBuilder stringBuilder = new StringBuilder();
						final String separator = ",";
						for (Item item : items) {
							stringBuilder.append(item.id);
							if (!items.equals(items.get(items.size() - 1))) {
								stringBuilder.append(separator);
							}
						}

						final String ids = stringBuilder.toString();
						youtubeRequest.videoListAsync(ids, new Callback<VideoList>() {
							@Override
							public void onResponse(Call<VideoList> call, Response<VideoList> response) {
								for (int i = 0; i < response.body().items.size(); i++) {
									searchResultViewModels.get(i).viewCount = response.body().items.get(i).statistics.viewCount;
								}

								adapter.setViewModels(searchResultViewModels);
							}

							@Override
							public void onFailure(Call<VideoList> call, Throwable t) {
							}
						});

					}

					@Override
					public void onFailure(Call<Search> call, Throwable t) {
						Toast.makeText(SearchActivity.this, "検索結果の取得に失敗しました", Toast.LENGTH_LONG).show();
					}
				});

				return false;
			}

			@Override
			public boolean onQueryTextChange(String newText) {
				return false;
			}
		});

		return true;
	}

	static class SearchResultViewModel {
		String id;
		String title;
		String channelTitle;
		String viewCount;
		String thumbnailUrl;

		SearchResultViewModel(String id , String title, String channelTitle, String viewCount, String thumbnailUrl) {
			this.id = id;
			this.title = title;
			this.channelTitle = channelTitle;
			this.viewCount = viewCount;
			this.thumbnailUrl = thumbnailUrl;
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
