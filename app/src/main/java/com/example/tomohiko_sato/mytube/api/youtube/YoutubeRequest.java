package com.example.tomohiko_sato.mytube.api.youtube;

import android.util.Log;

import com.example.tomohiko_sato.mytube.api.youtube.data.Search;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class YoutubeRequest {
	private final static String TAG = YoutubeRequest.class.getSimpleName();

	private final YoutubeAPI request;

	public YoutubeRequest() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://www.googleapis.com/youtube/v3/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		request = retrofit.create(YoutubeAPI.class);
	}

	public Response<Search> searchSync(String keyword) {
		Log.d(TAG, "keyword: " + keyword);

		Call<Search> searchRequest = request.search(keyword);
		Response<Search> response = null;
		try {
			response = searchRequest.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public void searchAsync(String keyword, Callback<Search> callback) {
		Call<Search> repo = request.search(keyword);
		repo.enqueue(callback);
	}
}
