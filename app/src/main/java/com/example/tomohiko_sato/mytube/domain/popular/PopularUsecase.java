package com.example.tomohiko_sato.mytube.domain.popular;

import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Popular;

import javax.inject.Inject;

import retrofit2.Callback;

public class PopularUseCase {
	YoutubeRequest youtubeRequest;

	@Inject
	public PopularUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
	}

	public void fetchPopular(Callback<Popular> callback) {
		youtubeRequest.fetchPopular(callback);
	}
}
