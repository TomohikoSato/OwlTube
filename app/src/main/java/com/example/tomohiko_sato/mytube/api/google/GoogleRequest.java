package com.example.tomohiko_sato.mytube.api.google;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GoogleRequest {
	private final static String TAG = GoogleRequest.class.getSimpleName();

	private final GoogleAPI request;

	public GoogleRequest() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("http://suggestqueries.google.com/complete/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		request = retrofit.create(GoogleAPI.class);
	}

	public void fetchSuggestKeywordForYoutube(String keyword, Callback<List<List<String>>> callback) {
		Call<List<List<String>>> call = request.suggestKeywordForYoutube(keyword);
		call.enqueue(callback);
	}


}
