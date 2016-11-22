package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.util.ArrayMap;
import android.util.Log;

import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.Item;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.VideoList;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.infra.api.mapper.VideoItemMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YoutubeRequest {
	private final static String TAG = YoutubeRequest.class.getSimpleName();
	private final YoutubeAPI api;

	@Inject
	public YoutubeRequest(YoutubeAPI api) {
		this.api = api;
	}

	public List<VideoItem> search(String keyword) {
		Log.d(TAG, "keyword: " + keyword);

		Call<Search> searchRequest = api.search(keyword);
		Response<Search> response = null;
		try {
			response = searchRequest.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response.body() == null) {
			return null;
		}

		List<VideoItem> items = VideoItemMapper.map(response.body());

		return items;
	}

	public Map<String, String> fetchStatistics(List<String> videoIds) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String videoId : videoIds) {
			sb.append(videoId).append(separator);
		}
		sb.deleteCharAt(sb.length() - 1);

		return fetchStatistics(sb.toString());
	}

	private Map<String, String> fetchStatistics(String videoIds) {
		Call<VideoList> call = api.videoListStatistics(videoIds);

		Map<String, String> map = new ArrayMap<>();
		try {
			Response<VideoList> response = call.execute();
			for (Item item : response.body().items) {
				map.put(item.id, item.statistics.viewCount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public List<VideoItem> fetchPopular() {
		Call<Popular> call = api.videoListPopular();
		Popular response = null;
		try {
			response = call.execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response == null) {
			return null;
		}

		return VideoItemMapper.map(response);
	}

	public List<VideoItem> fetchRealtedToVideoId(String videoId) {
		Call<Search> call = api.relatedToVideoId(videoId);

		Search response = null;
		try {
			response = call.execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return VideoItemMapper.map(response);
	}
}
