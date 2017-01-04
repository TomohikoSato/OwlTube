package com.example.tomohiko_sato.owltube.domain.popular;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;

import javax.inject.Inject;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class PopularUseCase {
	private final YoutubeRequest youtubeRequest;

	@Inject
	public PopularUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
	}

	public Single<VideoResponse> fetchPopular() {
		return youtubeRequest.fetchPopular(null).subscribeOn(Schedulers.io());
	}

	public Single<VideoResponse> fetchNextPopular(@NonNull final String pageToken) {
		return youtubeRequest.fetchPopular(pageToken).subscribeOn(Schedulers.io());
	}
}
