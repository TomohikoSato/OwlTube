package com.example.tomohiko_sato.myretrofit;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
	private static final String TAG = MainActivity.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

	}

	@Override
	protected void onResume() {
		super.onResume();

		AsyncTask<Void, Void, Response> task = new AsyncTask<Void, Void, Response>() {
			@Override
			protected Response doInBackground(Void... params) {
				OkHttpClient client = new OkHttpClient();
				Request request = new Request.Builder()
						.url("https://api.github.com/")
						.get()
						.build();
				Response response = null;
				try {
					response = client.newCall(request).execute();
					Log.d(TAG, String.format("Request: %s", response.toString()));
					Log.d(TAG, String.format("Request Body: %s", response.body().string()));
				} catch (IOException e) {
					e.printStackTrace();
				}

				return response;
			}
		};

		task.execute();
/*
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl("https://api.github.com/")
				.build();

		GithubRequest request = retrofit.create(GithubRequest.class);
		Call<Repo> repo = request.getUser("square");

		repo.enqueue(new Callback<Repo>() {
			@Override
			public void onResponse(Call<Repo> call, Response<Repo> response) {
				Log.d(TAG, String.format("Response: %s", response.body().login));

			}

			@Override
			public void onFailure(Call<Repo> call, Throwable t) {
				Log.d(TAG, String.format("Failure: %s", t.toString()));
			}
		});
*/
	}
}
