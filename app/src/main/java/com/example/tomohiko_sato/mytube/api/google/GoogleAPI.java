package com.example.tomohiko_sato.mytube.api.google;

import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

interface GoogleAPI {

	/**
	 * fetch suggest keyword for youtube
	 *
	 * 非公開APIなので無断で変更される可能性あり
	 * clientがfirefoxな理由：firefox向けのレスポンスが簡潔で扱いやすかったから
	 * ds=yt でyoutube向けのsuggestにしている
	 */
	@GET("search?client=firefox&ds=yt")
	Call<List<List<String>>> suggestKeywordForYoutube(@Query("q") String q);
}
