package com.example.tomohiko_sato.mytube.api.youtube;

import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;
import com.example.tomohiko_sato.mytube.api.youtube.data.statistics.VideoList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.tomohiko_sato.mytube.config.Key.Youtube.API_KEY;

interface YoutubeAPI {
	@GET("search?part=snippet&maxResults=20&key=" + API_KEY)
	Call<Search> search(@Query("q") String q);

	@GET("videos?part=statistics&key=" + API_KEY)
	Call<VideoList> videoListStatistics(@Query("id") String videoIds);

	@GET("videos?part=snippet&chart=mostPopular&regionCode=JP&maxResults=20&key=" + API_KEY)
	Call<Popular> videoListPopular();
}
