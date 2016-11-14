package com.example.tomohiko_sato.mytube.api.youtube;

import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.example.tomohiko_sato.mytube.config.Key.Youtube.API_KEY;

interface YoutubeAPI {
	@GET("search?part=snippet&maxResults=20&key=" + API_KEY)
	Call<Search> search(@Query("q") String q);
}
