package com.example.tomohiko_sato.mytube.infra.dao;

import java.util.List;

public interface SearchHistoryDao {
	void insertOrUpdateSearchHistory(String searchHistory);

	List<String> selectAllSearchHistories();
}
