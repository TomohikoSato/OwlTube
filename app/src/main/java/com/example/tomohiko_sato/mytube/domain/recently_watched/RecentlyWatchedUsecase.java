package com.example.tomohiko_sato.mytube.domain.recently_watched;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.tomohiko_sato.mytube.config.AppConst;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Popular;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javax.inject.Inject;

import retrofit2.Callback;

public class RecentlyWatchedUseCase {
	YoutubeRequest youtubeRequest;
	Context context;

	@Inject
	RecentlyWatchedUseCase(YoutubeRequest youtubeRequest, Context context) {
		this.youtubeRequest = youtubeRequest;
		this.context = context;
	}

	public void fetchRecentlyWatched(Callback<Popular> callback) {
		SharedPreferences recentlyWatchedSP = context.getSharedPreferences(AppConst.Pref.NAME, 0);
		Set<String> set = recentlyWatchedSP.getStringSet(AppConst.Pref.KEY_RECENTLY_WATCHED, new HashSet<String>());

		final ArrayList<String> videoIdList = new ArrayList<>();
		videoIdList.addAll(set);
		youtubeRequest.fetch(videoIdList, callback);
	}
}
