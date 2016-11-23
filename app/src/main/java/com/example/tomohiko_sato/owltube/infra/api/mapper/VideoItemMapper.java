package com.example.tomohiko_sato.owltube.infra.api.mapper;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Item;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.example.tomohiko_sato.owltube.infra.api.youtube.data} を {@link com.example.tomohiko_sato.owltube.domain.data.VideoItem} へ変換するクラス
 */
public class VideoItemMapper {
	public static List<VideoItem> map(@NonNull Search search) {
		List<VideoItem> result = new ArrayList<>();
		for (Item item : search.items) {
			result.add(new VideoItem(item.id.videoId, item.snippet.title, item.snippet.channelTitle, null, item.snippet.thumbnails.medium.url));
		}

		return result;
	}

	public static List<VideoItem> map(@NonNull Popular popular) {
		List<VideoItem> result = new ArrayList<>();
		for (com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Item item : popular.items) {
			if (isDeletedVideo(item)) {
				// Deleted Video はスキップする
				continue;
			}

			result.add(new VideoItem(item.id, item.snippet.title, item.snippet.channelTitle, item.statistics.viewCount, item.snippet.thumbnails.medium.url));
		}
		return result;
	}

	/**
	 * 削除されたVideoか判定する。Youtube APIに明確なフラグがないため、それっぽいので判定している。
	 */
	private static boolean isDeletedVideo(com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Item item) {
		return item.statistics == null;
	}
}
