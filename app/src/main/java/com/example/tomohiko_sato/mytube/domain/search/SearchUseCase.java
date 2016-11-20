package com.example.tomohiko_sato.mytube.domain.search;

import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.search.Search;

import javax.inject.Inject;

import retrofit2.Callback;

public class SearchUseCase {
	final YoutubeRequest youtubeRequest;

	@Inject
	public SearchUseCase(YoutubeRequest youtubeRequest) {
		this.youtubeRequest = youtubeRequest;
	}

	public void search(String query, Callback<Search> callback) {
		youtubeRequest.searchAsync(query, callback);
	}
}
