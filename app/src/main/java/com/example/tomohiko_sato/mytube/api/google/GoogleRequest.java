package com.example.tomohiko_sato.mytube.api.google;

import android.os.Handler;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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

	public static class RetrofitSuggestKeywordResponse {
		public String keyword;
		public List<String> suggests = new ArrayList<>();
	}

	public void fetchSuggestKeywordForYoutube(String keyword, Callback<List<RetrofitSuggestKeywordResponse>> callback) {
		Call<List<RetrofitSuggestKeywordResponse>> call = request.suggestKeywordForYoutube(keyword);
		call.enqueue(callback);
	}

	private final OkHttpClient client = new OkHttpClient();

	public static class SuggestResponse {
		public String result;
		public List<String> suggests = new ArrayList<>();
	}

	public void fetchSuggestKeywordForYoutube(String keyword, final SuggestResponse suggestResponse) throws IOException {
		final Request request = new Request.Builder()
				.url("http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=" + keyword)
				.get()
				.build();

		final Handler handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					final Response response = client.newCall(request).execute();
					Log.d(TAG, response.body().string());
					handler.post(new Runnable() {
						@Override
						public void run() {
							suggestResponse.result = response.body().toString();
//							suggestResponse.suggests = response.body().toString()
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
