package com.example.tomohiko_sato.mytube.infra.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.RawRes;

import com.example.tomohiko_sato.mytube.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.inject.Inject;

public class DefaultDBHelper extends SQLiteOpenHelper {

	private final static int DATABASE_VERSION = 1;
	private final static String DATABASE_NAME = "main.db"; // TODO: DBの名前どうするべきか調査
	private final Context context;

	@Inject
	public DefaultDBHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(readSql(R.raw.sql_recently_watched_create));
		db.execSQL(readSql(R.raw.sql_search_history_create));
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
	}

	public String readSql(@RawRes int rawResource) {
		BufferedReader br = new BufferedReader(new InputStreamReader(context.getResources().openRawResource(rawResource)));
		StringBuilder sb = new StringBuilder();

		String line;
		try {
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		String sql = sb.toString();
		return sql;
	}
}
