package com.example.tomohiko_sato.mytube.dao;

import java.util.List;

public interface RecentlyWatchedDao {

	List<VideoItem> selectOrderByRecentlyCreated(int limit);

	/**
	 * テーブル内に存在しない{@link VideoItem#videoId}であればInsert、既に存在すればUpdateする。
	 */
	void insertOrUpdate(VideoItem item);
}
