package com.example.tomohiko_sato.mytube.domain.popular;

import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Popular;

import retrofit2.Callback;

public class PopularUseCase {
	public void fetchPopular(Callback<Popular> callback) {
		new YoutubeRequest().fetchPopular(callback);
	}
}
