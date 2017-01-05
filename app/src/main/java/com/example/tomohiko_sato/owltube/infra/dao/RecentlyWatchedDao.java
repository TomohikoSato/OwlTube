package com.example.tomohiko_sato.owltube.infra.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.infra.db.DefaultDBHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedDao {
	private final static int COLUMN_VIDEO_ID = 0;
	private final static int COLUMN_TITLE = 1;
	private final static int COLUMN_CHANNEL_TITLE = 2;
	private final static int COLUMN_VIEW_COUNT = 3;
	private final static int COLUMN_THUMNBNAIL_URL = 4;
	private final static int COLUMN_CREATED_AT = 5;
	private final static int COLUMN_UPDATED_AT = 6;

	private final DefaultDBHelper helper;

	@Inject
	public RecentlyWatchedDao(DefaultDBHelper helper) {
		this.helper = helper;
	}

	public List<Video> selectAllOrderByRecentlyCreated(int limit) {
		List<Video> list = new ArrayList<>();
		try (SQLiteDatabase db = helper.getReadableDatabase();
			 Cursor c = db.rawQuery(helper.readSql(R.raw.sql_recently_watched_select_all), new String[]{String.valueOf(limit)})) {

			for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				list.add(new Video(c.getString(COLUMN_VIDEO_ID),
						c.getString(COLUMN_TITLE),
						c.getString(COLUMN_CHANNEL_TITLE),
						c.getString(COLUMN_VIEW_COUNT),
						c.getString(COLUMN_THUMNBNAIL_URL)
				));
			}
		}

		return list;
	}


	/**
	 * テーブル内に存在しない{@link Video#videoId}であればInsert、既に存在すればUpdateする。
	 *
	 * @throws android.database.SQLException
	 */
	public void add(@NonNull Video item) {
		if (selectByVideoId(item.videoId) == null) {
			insert(item);
		} else {
			update(item);
		}
	}

	private Video selectByVideoId(String videoId) {
		Video item = null;
		try (SQLiteDatabase db = helper.getReadableDatabase();
			 Cursor c = db.rawQuery(helper.readSql(R.raw.sql_recently_watched_select_by_video_id), new String[]{videoId})) {
			if (c.moveToFirst()) {
				item = new Video(c.getString(COLUMN_VIDEO_ID),
						c.getString(COLUMN_TITLE),
						c.getString(COLUMN_CHANNEL_TITLE),
						c.getString(COLUMN_VIEW_COUNT),
						c.getString(COLUMN_THUMNBNAIL_URL));
			}
		}

		return item;
	}

	/**
	 * @throws android.database.SQLException
	 */
	private void update(Video item) {
		try (SQLiteDatabase db = helper.getWritableDatabase();
			 SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_recently_watched_update))) {
			stmt.bindString(1, item.title);
			stmt.bindString(2, item.channelTitle);
			stmt.bindString(3, item.viewCount);
			stmt.bindString(4, item.thumbnailUrl);
			stmt.bindLong(5, System.currentTimeMillis() / 1000L);
			stmt.bindString(6, item.videoId);
			stmt.execute();
		}
	}

	/**
	 * @throws android.database.SQLException
	 */
	private void insert(Video item) {
		try (SQLiteDatabase db = helper.getWritableDatabase();
			 SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_recently_watched_insert))) {
			stmt.bindString(1, item.videoId);
			stmt.bindString(2, item.title);
			stmt.bindString(3, item.channelTitle);
			stmt.bindString(4, item.viewCount);
			stmt.bindString(5, item.thumbnailUrl);
			stmt.bindLong(6, System.currentTimeMillis() / 1000L);
			stmt.bindLong(7, System.currentTimeMillis() / 1000L);
			stmt.execute();
		}
	}
}
