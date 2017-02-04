package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;

import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.mapper.VideoMapper;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.Item;
import com.example.tomohiko_sato.owltube.util.Logger;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Single;


public class YoutubeRequest {
	private final YoutubeAPI api;

	@Inject
	public YoutubeRequest(YoutubeAPI api) {
		this.api = api;
	}

	public Single<VideoResponse> search(String keyword, @Nullable String pageToken) {
		Logger.d("keyword: " + keyword);
		return api.search(keyword, pageToken).map(VideoMapper::map);
	}

	/**
	 * ViewCount(視聴回数)を取得する
	 */
	public Single<Map<String, String>> fetchViewCount(@NonNull List<String> videoIds) {
		if (videoIds.size() == 0) {
			Single.error(new IllegalArgumentException());
		}

		return api.videoListStatistics(toCommaSeparetedString(videoIds))
				.map(videoList -> {
					Map<String, String> map = new ArrayMap<>();
					for (Item item : videoList.items) {
						map.put(item.id, item.statistics.viewCount);
					}
					return map;
				});
	}

	public Single<VideoResponse> fetchPopular(@Nullable String pageToken) {
		return api.videoListPopular(pageToken).map(VideoMapper::map);
	}

	public Single<VideoResponse> fetchRealtedToVideoId(String videoId) {
		return api.relatedToVideoId(videoId).map(VideoMapper::map);
	}

	private String toCommaSeparetedString(List<String> items) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String item : items) {
			sb.append(item).append(separator);
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}
}
