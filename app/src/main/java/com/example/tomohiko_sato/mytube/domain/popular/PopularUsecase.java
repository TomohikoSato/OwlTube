package com.example.tomohiko_sato.mytube.domain.popular;

import android.os.Handler;

import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.util.Callback;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;

import java.util.List;

import javax.inject.Inject;

public class PopularUseCase {
	YoutubeRequest youtubeRequest;

	@Inject
	public PopularUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
	}

	public void fetchPopular(final Callback<List<VideoItem>> callback) {
		final Handler handler = new Handler();

		new Thread(new Runnable() {
			@Override
			public void run() {
				final List<VideoItem> list = youtubeRequest.fetchPopular();
				handler.post(new Runnable() {
					@Override
					public void run() {
						callback.onSuccess(list);
					}
				});
			}
		}).start();
	}
}
