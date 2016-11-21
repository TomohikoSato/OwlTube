package com.example.tomohiko_sato.mytube.infra.dao;

import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

import java.util.List;

import javax.inject.Inject;

public class SearchHistoryDaoImpl implements SearchHistoryDao{
	private final DefaultDBHelper helper;

	@Inject
	public SearchHistoryDaoImpl(DefaultDBHelper helper) {
		this.helper = helper;
	}

	public void insertOrUpdateSearchHistory(String searchHistory) {

	}

	public List<String> selectAllSearchHistories() {

	}
}
