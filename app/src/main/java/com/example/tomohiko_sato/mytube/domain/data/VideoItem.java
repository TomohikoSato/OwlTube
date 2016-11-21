package com.example.tomohiko_sato.mytube.domain.data;

public class VideoItem {
	public String videoId;
	public String title;
	public String channelTitle;
	public String viewCount;
	public String thumbnailUrl;

	public VideoItem(String videoId, String title, String channelTitle, String viewCount, String thumbnailUrl) {
		this.videoId = videoId;
		this.title = title;
		this.channelTitle = channelTitle;
		this.viewCount = viewCount;
		this.thumbnailUrl = thumbnailUrl;
	}
}
