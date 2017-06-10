package com.example.tomohiko_sato.owltube.infra.api.youtube;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.example.tomohiko_sato.owltube.BuildConfig;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.statistics.VideoList;

import io.reactivex.Observable;
import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface YoutubeAPI {
	int MAX_RESULTS = 50;

	@GET("search?part=snippet&regionCode=JP&type=video&maxResults=" + MAX_RESULTS + "&key=" + BuildConfig.API_KEY)
	Single<Search> search(@NonNull @Query("q") String q, @Nullable @Query("pageToken") String pageToken);

	@GET("videos?part=statistics&key=" + BuildConfig.API_KEY)
	Single<VideoList> videoListStatistics(@NonNull @Query("id") String videoIds);

	@GET("videos?part=snippet,statistics&chart=mostPopular&regionCode=JP&maxResults=" + MAX_RESULTS + "&key=" + BuildConfig.API_KEY)
	Single<Popular> videoListPopular(@Nullable @Query("pageToken") String pageToken);

	@GET("search?part=snippet&maxResults=10&type=video&key=" + BuildConfig.API_KEY)
	Single<Search> relatedToVideoId(@NonNull @Query("relatedToVideoId") String videoId);
}
