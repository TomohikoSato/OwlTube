package com.example.tomohiko_sato.mytube.infra.dao;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.mytube.domain.data.VideoItem;

import java.util.List;

public interface RecentlyWatchedDao {

	List<VideoItem> selectAllOrderByRecentlyCreated(int limit);

	/**
	 * テーブル内に存在しない{@link VideoItem#videoId}であればInsert、既に存在すればUpdateする。
	˙ */
	void insertOrUpdate(@NonNull VideoItem item);
}
