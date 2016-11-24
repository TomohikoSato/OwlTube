package com.example.tomohiko_sato.owltube.domain.popular;

import android.os.Handler;
import android.support.annotation.Nullable;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;

import java.util.List;

import javax.inject.Inject;

public class PopularUseCase {
	private final YoutubeRequest youtubeRequest;

	@Inject
	public PopularUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
	}

	public void fetchPopular(final Callback<VideoResponse> callback ) {
		fetchPopular(null, callback);
	}

	public void fetchPopular(@Nullable final String pageToken, final Callback<VideoResponse> callback ) {
		final Handler handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				final VideoResponse response = youtubeRequest.fetchPopular(pageToken);
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(response);
					}
				});
			}
		}).start();
	}
}
