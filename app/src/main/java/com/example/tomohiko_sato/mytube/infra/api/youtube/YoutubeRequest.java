package com.example.tomohiko_sato.mytube.infra.api.youtube;

import android.util.Log;

import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.statistics.VideoList;

import java.io.IOException;
import java.util.List;

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

	public void searchAsync(String keyword, Callback<Search> callback) {
		Call<Search> repo = api.search(keyword);
		repo.enqueue(callback);
	}

	public Response<Search> searchSync(String keyword) {
		Log.d(TAG, "keyword: " + keyword);

		Call<Search> searchRequest = api.search(keyword);
		Response<Search> response = null;
		try {
			response = searchRequest.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public void fetchStatistics(String videoIds, Callback<VideoList> callback) {
		Call<VideoList> repo = api.videoListStatistics(videoIds);
		repo.enqueue(callback);
	}

	public void fetchStatistics(List<String> videoIds, Callback<VideoList> callback) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String videoId : videoIds) {
			sb.append(videoId).append(separator);
		}

		sb.deleteCharAt(sb.length() - 1);

		Call<VideoList> repo = api.videoListStatistics(sb.toString());
		repo.enqueue(callback);
	}

	public void fetch(List<String> videoIds, Callback<Popular> callback) {
		final StringBuilder sb = new StringBuilder();
		final String separator = ",";
		for (String videoId : videoIds) {
			sb.append(videoId).append(separator);
		}

		sb.deleteCharAt(sb.length() - 1);

		Call<Popular> repo = api.videoListByIds(sb.toString());
		repo.enqueue(callback);
	}

	public void fetchPopular(Callback<Popular> callback) {
		Call<Popular> repo = api.videoListPopular();
		repo.enqueue(callback);
	}
}
