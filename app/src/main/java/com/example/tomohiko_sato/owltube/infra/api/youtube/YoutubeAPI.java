package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tomohiko_sato.owltube.infra.api.youtube.config.Api;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeAPI {
	int MAX_RESULTS = 50;

	@GET("search?part=snippet&regionCode=JP&type=video&maxResults=" + MAX_RESULTS + "&key=" + Api.API_KEY)
	Call<Search> search(@NonNull @Query("q") String q, @Nullable @Query("pageToken") String pageToken);

	@GET("videos?part=statistics&key=" + Api.API_KEY)
	Call<VideoList> videoListStatistics(@NonNull @Query("id") String videoIds);

	@GET("videos?part=snippet,statistics&chart=mostPopular&regionCode=JP&maxResults=" + MAX_RESULTS + "&key=" + Api.API_KEY)
	Call<Popular> videoListPopular(@Nullable @Query("pageToken") String pageToken);

	@GET("search?part=snippet&maxResults=10&type=video&key=" + Api.API_KEY)
	Call<Search> relatedToVideoId(@NonNull @Query("relatedToVideoId") String videoId);
}
