package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.api.youtube.data.Item;
import com.example.tomohiko_sato.mytube.api.youtube.data.Search;
import com.example.tomohiko_sato.mytube.api.youtube.data.Snippet;
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
				PlayerActivity.startPlayerActivity(SearchActivity.this, adapter.getItem(position).id.videoId);
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
						adapter.setItems(response.body().items);
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

	static class SearchResultListAdapter extends ArrayAdapter<Item> {
		private List<Item> items = new ArrayList<>();
		private final Context context;
		private final LayoutInflater inflater;

		public SearchResultListAdapter(Context context) {
			super(context, R.layout.search_result_list_item);
			this.context = context;
			this.inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
		}

		public void setItems(List<Item> items) {
			this.items = items;
			notifyDataSetChanged();
		}

		@Override
		public int getCount() {
			return items.size();
		}

		@Override
		public Item getItem(int position) {
			return items.get(position);
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
				TextView createdAt = (TextView) convertView.findViewById(R.id.created_at);
				ImageView thumbnail = (ImageView) convertView.findViewById(R.id.thumbnail);
				holder = new ViewHolder(title, channelTitle, createdAt, thumbnail);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			Snippet snippet = items.get(position).snippet;
			holder.title.setText(snippet.title);
			holder.publishedAt.setText(snippet.publishedAt);
			holder.channelTitle.setText(snippet.channelTitle);
			Picasso.with(context).load(snippet.thumbnails.medium.url).into(holder.thumbnail);

			return convertView;
		}

		static class ViewHolder {
			TextView title;
			TextView channelTitle;
			TextView publishedAt;
			ImageView thumbnail;

			ViewHolder(TextView title, TextView channelTitle, TextView createdAt, ImageView thumbnail) {
				this.title = title;
				this.channelTitle = channelTitle;
				this.publishedAt = createdAt;
				this.thumbnail = thumbnail;
			}
		}
	}
}
