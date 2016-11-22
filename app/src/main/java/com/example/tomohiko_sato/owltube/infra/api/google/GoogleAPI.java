package com.example.tomohiko_sato.owltube.infra.api.google;

public interface GoogleAPI {

	/**
	 * fetch suggest keyword for youtube
	 *
	 * 非公開APIなので無断で変更される可能性あり
	 * clientがfirefoxな理由：firefox向けのレスポンスが簡潔で扱いやすかったから
	 * ds=yt でyoutube向けのsuggestにしている
	 */
/*	@GET("search?client=firefox&ds=yt")
	Call<List<GoogleRequest.RetrofitSuggestKeywordResponse>> suggestKeywordForYoutube(@Query("q") String q);*/
}
