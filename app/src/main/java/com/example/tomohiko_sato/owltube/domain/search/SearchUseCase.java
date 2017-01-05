package com.example.tomohiko_sato.owltube.domain.search;

import android.support.annotation.Nullable;
import android.support.v4.util.Pair;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.google.GoogleRequest;
import com.example.tomohiko_sato.owltube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.owltube.infra.dao.SearchHistoryDao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class SearchUseCase {
	private final YoutubeRequest youtubeRequest;
	private final GoogleRequest googleRequest;
	private final SearchHistoryDao dao;

	@Inject
	public SearchUseCase(YoutubeRequest youtubeRequest, GoogleRequest googleRequest, SearchHistoryDao dao) {
		this.youtubeRequest = youtubeRequest;
		this.googleRequest = googleRequest;
		this.dao = dao;
	}

	/**
	 * @return Single<VideoResponse> 検索結果, Completable 検索履歴の追加に成功したか
	 */
/*
	public Pair<Single<VideoResponse>, Completable> search(final String query, @Nullable final String pageToken) {
		Single<VideoResponse> response = youtubeRequest.search(query, pageToken)
				.map(videoResponse -> {
							final List<Video> videos = videoResponse.videos;
							List<String> videoIds = new ArrayList<>();
							for (Video video : videos) {
								videoIds.add(video.videoId);
							}

							Map<String, String> map = youtubeRequest.fetchViewCount(videoIds).blockingGet();
							for (Video video : videos) {
								video.viewCount = map.get(video.videoId);
							}

							return videoResponse;
						}
				);

		return Pair.create(response, addSearchHistory(query));
	}
*/

	public Maybe<VideoResponse> search(final String query, @Nullable final String pageToken) {
		Single<VideoResponse> response = youtubeRequest.search(query, pageToken)
				.map(videoResponse -> {
							final List<Video> videos = videoResponse.videos;
							List<String> videoIds = new ArrayList<>();
							for (Video video : videos) {
								videoIds.add(video.videoId);
							}

							Map<String, String> map = youtubeRequest.fetchViewCount(videoIds).blockingGet();
							for (Video video : videos) {
								video.viewCount = map.get(video.videoId);
							}

							return videoResponse;
						}
				);



		return Pair.create(response, addSearchHistory(query));
	}


	public Observable<List<String>> fetchSuggest(final String query) {
		return googleRequest.fetchSuggestKeywordForYoutube(query)
				.subscribeOn(Schedulers.io());
	}

	private Completable addSearchHistory(final String searchHistory) {
		return dao.addSearchHistory(searchHistory)
				.subscribeOn(Schedulers.io());
	}

	public Single<List<String>> fetchSearchHistories() {
		return dao.selectAllSearchHistories()
				.subscribeOn(Schedulers.io());
	}
}
