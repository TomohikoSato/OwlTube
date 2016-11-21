package com.example.tomohiko_sato.mytube.domain.search;

import android.os.Handler;


import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.util.Callback;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.search.Search;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;


public class SearchUseCase {
	final YoutubeRequest youtubeRequest;

	@Inject
	public SearchUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
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

				List<String> videoIds = null;
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
		});
	}
}
