package com.example.tomohiko_sato.owltube.infra.api.mapper;

import android.support.annotation.NonNull;

import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Item;
import com.example.tomohiko_sato.owltube.infra.api.youtube.data.search.Search;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link com.example.tomohiko_sato.owltube.infra.api.youtube.data} を {@link Video} へ変換するクラス
 */
public class VideoMapper {
	public static VideoResponse map(@NonNull Search search) {
		List<Video> videos = new ArrayList<>();
		for (Item item : search.items) {
			videos.add(new Video(item.id.videoId, item.snippet.title, item.snippet.channelTitle, null, item.snippet.thumbnails.medium.url));
		}
		return new VideoResponse(videos, search.nextPageToken);
	}

	public static VideoResponse map(@NonNull Popular popular) {
		List<Video> videos = new ArrayList<>();
		for (com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Item item : popular.items) {
			if (isDeletedVideo(item)) {
				continue;  // Deleted Video はスキップする
			}
			videos.add(new Video(item.id, item.snippet.title, item.snippet.channelTitle, item.statistics.viewCount, item.snippet.thumbnails.medium.url));
		}
		return new VideoResponse(videos, popular.nextPageToken);
	}

	/**
	 * 削除されたVideoか判定する。Youtube APIに明確なフラグがないため、それっぽいので判定している。
	 */
	private static boolean isDeletedVideo(com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular.Item item) {
		return item.statistics == null;
	}
}
