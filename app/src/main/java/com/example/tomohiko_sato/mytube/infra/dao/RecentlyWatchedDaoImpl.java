package com.example.tomohiko_sato.mytube.infra.dao;

import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedDaoImpl implements RecentlyWatchedDao {
	DefaultDBHelper helper;

	@Inject
	public RecentlyWatchedDaoImpl(DefaultDBHelper helper) {
		this.helper = helper;
	}

	@Override
	public void selectOrderByRecentlyCreated(int limit, Callback<List<VideoItem>> callback) {

	}

	@Override
	public void insertOrUpdate(VideoItem item) {

	}
}
