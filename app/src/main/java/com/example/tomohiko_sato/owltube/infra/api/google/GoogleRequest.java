package com.example.tomohiko_sato.owltube.infra.api.google;

import com.example.tomohiko_sato.owltube.infra.api.mapper.SuggestMapper;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleRequest {
	private final static String TAG = GoogleRequest.class.getSimpleName();

	private final OkHttpClient client = new OkHttpClient();

	public GoogleRequest() {
	}

	public List<String> fetchSuggestKeywordForYoutube(String keyword) {
		final Request request = new Request.Builder()
				.url("http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=" + keyword)
				.get()
				.build();

		Response response = null;
		try {
			response = client.newCall(request).execute();
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (response == null) {
			return null;
		}

		try {
			return SuggestMapper.map(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}