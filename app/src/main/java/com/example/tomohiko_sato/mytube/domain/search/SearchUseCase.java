package com.example.tomohiko_sato.mytube.domain.search;

import android.content.SharedPreferences;
import android.os.Handler;


import com.example.tomohiko_sato.mytube.config.AppConst;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.util.Callback;
import com.example.tomohiko_sato.mytube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.mytube.infra.dao.SearchHistoryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class SearchUseCase {
	private final YoutubeRequest youtubeRequest;
	private final GoogleRequest googleRequest;
	private final SearchHistoryDao dao;

	@Inject
	public SearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest, SearchHistoryDao dao) {
		this.youtubeRequest = youtubeRequest;
		this.googleRequest = googleRequest;
		this.dao = dao;
	}

	public void search(final String query, final Callback<List<VideoItem>> callback) {
		final Handler handler = new Handler();
		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<VideoItem> items = youtubeRequest.search(query);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(items);
					}
				});

				List<String> videoIds = new ArrayList<>();
				for (VideoItem item : items) {
					videoIds.add(item.videoId);
				}

				Map<String, String> map = youtubeRequest.fetchStatistics(videoIds);

				for (VideoItem item : items) {
					item.viewCount = map.get(item.videoId);
				}

				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(items);
					}
				});
			}
		}).start();
	}

	public void fetchSuggest(final String query, final Callback<List<String>> callback) {
		final Handler handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<String> suggests = googleRequest.fetchSuggestKeywordForYoutube(query);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(suggests);
					}
				});
			}
		}).start();
	}

	public void addSearchHistory(String searchHistory) {
		dao.insertOrUpdateSearchHistory(searchHistory);
	}

	public List<String> fetchSearchHistories() {
		return dao.selectAllSearchHistories();
	}
}
