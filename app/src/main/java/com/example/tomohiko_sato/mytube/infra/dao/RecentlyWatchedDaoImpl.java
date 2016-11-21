package com.example.tomohiko_sato.mytube.infra.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedDaoImpl implements RecentlyWatchedDao {
	private final static int COLUMN_VIDEO_ID = 0;
	private final static int COLUMN_TITLE = 1;
	private final static int COLUMN_CHANNEL_TITLE = 2;
	private final static int COLUMN_VIEW_COUNT = 3;
	private final static int COLUMN_THUMNBNAIL_URL = 4;
	private final static int COLUMN_CREATED_AT = 5;
	private final static int COLUMN_UPDATED_AT = 6;

	private final DefaultDBHelper helper;

	@Inject
	public RecentlyWatchedDaoImpl(DefaultDBHelper helper) {
		this.helper = helper;
	}

	@Override
	public List<VideoItem> selectAllOrderByRecentlyCreated(int limit) {
		List<VideoItem> list = new ArrayList<>();
		try (SQLiteDatabase db = helper.getReadableDatabase();
			 Cursor c = db.rawQuery(helper.readSql(R.raw.sql_video_select_all), new String[]{Integer.toString(limit)})) {
			c.moveToFirst();
			while (c.moveToNext()) {
				list.add(new VideoItem(c.getString(COLUMN_VIDEO_ID),
						c.getString(COLUMN_TITLE),
						c.getString(COLUMN_CHANNEL_TITLE),
						c.getString(COLUMN_VIEW_COUNT),
						c.getString(COLUMN_THUMNBNAIL_URL)
				));
			}
		}

		return list;
	}

	@Override
	/**
	 * @throws android.database.SQLException
	 */
	public void insertOrUpdate(VideoItem item) {
		if (selectByVideoId(item.videoId) == null) {
			insert(item);
		} else {
			update(item);
		}
	}

	private VideoItem selectByVideoId(String videoId) {
		VideoItem item = null;
		try (SQLiteDatabase db = helper.getReadableDatabase();
			 Cursor c = db.rawQuery(helper.readSql(R.raw.sql_video_select_by_video_id), new String[]{videoId})) {
			if (c.getCount() == 1) {
				item = new VideoItem(c.getString(COLUMN_VIDEO_ID),
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
	private void update(VideoItem item) {
		try (SQLiteDatabase db = helper.getWritableDatabase()) {
			SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_video_update));
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
	private void insert(VideoItem item) {
		try (SQLiteDatabase db = helper.getWritableDatabase()) {
			SQLiteStatement stmt = db.compileStatement(helper.readSql(R.raw.sql_video_insert));
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
