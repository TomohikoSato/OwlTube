package com.example.tomohiko_sato.owltube.infra.dao;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.Video;

import java.util.List;

public interface RecentlyWatchedDao {

	List<Video> selectAllOrderByRecentlyCreated(int limit);

	/**
	 * テーブル内に存在しない{@link Video#videoId}であればInsert、既に存在すればUpdateする。
	˙ */
	void insertOrUpdate(@NonNull Video item);
}
