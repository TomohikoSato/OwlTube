package com.example.tomohiko_sato.owltube.infra.api.mapper;


import android.util.Log;


import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class SuggestMapper {
	private final static String TAG = SuggestMapper.class.getSimpleName();

	public static List<String> map(String json) {
		List<String> result = new ArrayList<>();

		Log.d(TAG, json);
		try {
			JSONArray jsonArray = new JSONArray(json);
			String str = jsonArray.getString(1);
			JSONArray suggestJsonArray = new JSONArray(str);
			for (int i = 0; i < suggestJsonArray.length(); i++) {
				result.add(suggestJsonArray.getString(i));
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

		return result;
	}
}
