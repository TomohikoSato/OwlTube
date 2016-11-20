package com.example.tomohiko_sato.mytube.infra.dao;

import java.util.List;

public interface RecentlyWatchedDao {

	void selectOrderByRecentlyCreated(int limit, Callback<List<VideoItem>> callback);

	/**
	 * テーブル内に存在しない{@link VideoItem#videoId}であればInsert、既に存在すればUpdateする。
	 */
	void insertOrUpdate(VideoItem item);

	class Callback<T> {
		public void onSuccess(List<T> items) {

		}

		public void onFailure() {

		}

	}
}
