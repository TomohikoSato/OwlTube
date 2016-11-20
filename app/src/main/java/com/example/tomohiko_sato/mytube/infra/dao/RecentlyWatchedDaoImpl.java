package com.example.tomohiko_sato.mytube.infra.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.infra.db.DefaultDBHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class RecentlyWatchedDaoImpl implements RecentlyWatchedDao {
	DefaultDBHelper helper;

	@Inject
	public RecentlyWatchedDaoImpl(DefaultDBHelper helper) {
		this.helper = helper;

	}

	@Override
	public List<VideoItem> selectAllOrderByRecentlyCreated(int limit) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery(helper.readSql(R.raw.sql_video_select_all), limit);
		List<VideoItem> list = new ArrayList<VideoItem>();

		c.moveToFirst();
		

		return list;
	}

	private final static int COLUMN_VIDEO_ID = 0;
	private final static int COLUMN_TITLE = 1;
	private final static int COLUMN_CHANNEL_TITLE = 2;
	private final static int COLUMN_VIEW_COUNT = 3;
	private final static int COLUMN_THUMNBNAIL_URL = 4;
	private final static int COLUMN_CREATED_AT = 5;
	private final static int COLUMN_UPDATED_AT = 6;

	@Override
	public VideoItem selectByVideoId(String videoId) {
		SQLiteDatabase db = helper.getReadableDatabase();
		Cursor c = db.rawQuery(helper.readSql(R.raw.sql_video_select_by_video_id), new String[]{videoId});
		VideoItem item = null;
		try {
			if (c.getCount() == 1) {
				item = new VideoItem(c.getString(COLUMN_VIDEO_ID),
						c.getString(COLUMN_TITLE),
						c.getString(COLUMN_CHANNEL_TITLE),
						c.getString(COLUMN_VIEW_COUNT),
						c.getString(COLUMN_THUMNBNAIL_URL),
						c.getString(COLUMN_CREATED_AT),
						c.getString(COLUMN_UPDATED_AT));
			}
		} finally {
			c.close();
		}

		return item;
	}

	@Override
	public void insertOrUpdate(VideoItem item) {

	}
}
