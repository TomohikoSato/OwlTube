package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.Item;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.VideoList;
import com.example.tomohiko_sato.owltube.infra.api.mapper.VideoMapper;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Response;


public class YoutubeRequest {
	private final static String TAG = YoutubeRequest.class.getSimpleName();
	private final YoutubeAPI api;

	@Inject
	public YoutubeRequest(YoutubeAPI api) {
		this.api = api;
	}

	public VideoResponse search(String keyword) {
		Log.d(TAG, "keyword: " + keyword);

		Call<Search> searchRequest = api.search(keyword, null);
		Response<Search> response = null;
		try {
			response = searchRequest.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response.body() == null) {
			return null;
		}

		return VideoMapper.map(response.body());
	}

	/**
	 * ViewCount(視聴回数)を取得する
	 *
	 * @return Map<Key:videoId, Value:viewCount>
	 */
	public Map<String, String> fetchViewCount(@NonNull List<String> videoIds) {
		Call<VideoList> call = api.videoListStatistics(toCommaSeparetedString(videoIds));

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

	private String toCommaSeparetedString(List<String> items) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String item : items) {
			sb.append(item).append(separator);
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public VideoResponse fetchPopular(@Nullable String pageToken) {
		Call<Popular> call = api.videoListPopular(pageToken);
		Popular response = null;
		try {
			response = call.execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response == null) {
			return null;
		}

		return VideoMapper.map(response);
	}

	public VideoResponse fetchRealtedToVideoId(String videoId) {
		Call<Search> call = api.relatedToVideoId(videoId);

		Search response = null;
		try {
			response = call.execute().body();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return VideoMapper.map(response);
	}
}
