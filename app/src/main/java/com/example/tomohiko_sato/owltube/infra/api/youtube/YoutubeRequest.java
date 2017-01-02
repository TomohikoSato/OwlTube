package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.ArrayMap;
import android.util.Log;

import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.mapper.VideoMapper;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.Item;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.VideoList;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.Response;


public class YoutubeRequest {
	private final static String TAG = YoutubeRequest.class.getSimpleName();
	private final YoutubeAPI api;

	@Inject
	public YoutubeRequest(YoutubeAPI api) {
		this.api = api;
	}

	public Observable<VideoResponse> search(String keyword, @Nullable String pageToken) {
		Log.d(TAG, "keyword: " + keyword);
		return api.search(keyword, pageToken).map(VideoMapper::map);
	}

	/**
	 * ViewCount(視聴回数)を取得する
	 *
	 * @return Map<Api:videoId, Value:viewCount>
	 */
	public Map<String, String> fetchViewCount(@NonNull List<String> videoIds) {
		Map<String, String> map = new ArrayMap<>();
		if (videoIds.size() == 0) {
			return map;
		}

		Call<VideoList> call = api.videoListStatistics(toCommaSeparetedString(videoIds));

		try {
			//TODO: 畳み込み演算?とかできるのか試す
			Response<VideoList> response = call.execute();
			for (Item item : response.body().items) {
				map.put(item.id, item.statistics.viewCount);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	public Observable<Map<String, String>> fetchViewCountObservable(@NonNull List<String> videoIds) {
		if (videoIds.size() == 0) {
			Observable.error(new IllegalArgumentException());
		}

		return api.videoListStatisticsObservable(toCommaSeparetedString(videoIds))
				.map(videoList -> {
					Map<String, String> map = new ArrayMap<>();
					for (Item item : videoList.items) {
						map.put(item.id, item.statistics.viewCount);
					}
					return map;
				});
	}


	private String toCommaSeparetedString(List<String> items) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String item : items) {
			sb.append(item).append(separator);
		}
		return sb.deleteCharAt(sb.length() - 1).toString();
	}

	public Observable<VideoResponse> fetchPopular(@Nullable String pageToken) {
		return api.videoListPopular(pageToken).map(VideoMapper::map);
	}

	public Observable<VideoResponse> fetchRealtedToVideoId(String videoId) {
		return api.relatedToVideoId(videoId).map(VideoMapper::map);
	}
}
