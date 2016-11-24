package com.example.tomohiko_sato.owltube.domain.data;

import java.util.List;

/**
 * YoutubeAPIの動画一覧系APIのレスポンスから、ドメイン層で使用する値をまとめたクラス
 */
public class VideoResponse {
	public final List<Video> videos;

	/**
	 * YoutubeAPIで使用される、ページングで使うToken。videosの次のvideosが取れる。
	 */
	public final String pageToken;

	public VideoResponse(List<Video> items, String pageToken) {
		this.videos = items;
		this.pageToken = pageToken;
	}
}
