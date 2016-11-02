package com.example.tomohiko_sato.mytube.api.github;

import android.util.Log;

import java.io.IOException;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


/**
 * Created by tomohiko_sato on 10/27/16.
 */

public class GithubRequest {
	private final static String TAG = GithubRequest.class.getSimpleName();

	private final GithubAPI request;

	public GithubRequest() {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		request = retrofit.create(GithubAPI.class);
	}

	public Response<User> getUserSync(String name) {
		Log.d(TAG, "user: " + name );

		Call<User> repo = request.getUser(name);
		Response<User> response = null;
		try {
			response = repo.execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return response;
	}

	public void getUserAsync(String name, Callback<User> callback) {
		Call<User> repo = request.getUser(name);
		repo.enqueue(callback);
	}
}
