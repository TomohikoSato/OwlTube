package com.example.tomohiko_sato.mytube.infra.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class SearchHistoryDaoImpl implements SearchHistoryDao {
	private final static int COLUMN_QUERY = 0;

	private final DefaultDBHelper helper;

	@Inject
	public SearchHistoryDaoImpl(DefaultDBHelper helper) {
		this.helper = helper;
	}

	public List<String> selectAllSearchHistories() {
		List<String> result = new ArrayList<>();
		try (SQLiteDatabase db = helper.getReadableDatabase();
			 Cursor c = db.rawQuery(helper.readSql(R.raw.sql_search_history_select_all), null)) {

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				result.add((c.getString(COLUMN_QUERY)));
			}
		}

		return result;
	}

	//TODO: 1以上あったら削除すればいいのかも
	public void insertOrUpdateSearchHistory(String query) {
		try (SQLiteDatabase db = helper.getWritableDatabase()) {
			if (countByQuery(query, db) == 0) {
				insert(query, db);
			} else {
				update(query, db);
			}
		}
	}

	private int countByQuery(String query, SQLiteDatabase db) {
		try (Cursor c = db.rawQuery(helper.readSql(R.raw.sql_search_history_count_by_query), new String[]{query})) {
			return c.getCount();
		}
	}

	private void update(String query, SQLiteDatabase db) {
		try (SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_search_history_update))) {
			stmt.bindString(1, query);
			stmt.bindLong(2, System.currentTimeMillis() / 1000L);
			stmt.executeUpdateDelete();
		}
	}

	private void insert(String query, SQLiteDatabase db) {
		try (SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_search_history_insert))) {
			stmt.bindString(1, query);
			stmt.bindLong(2, System.currentTimeMillis() / 1000L);
			stmt.executeInsert();
		}
	}
}
