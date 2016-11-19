package com.example.tomohiko_sato.mytube.dao;

public class VideoItem {
	String videoId;
	String title;
	String channelTitle;
	String viewCount;
	String thumbnailUrl;
	String createdAt;
	String updatedAt;

	public VideoItem(String videoId, String title, String channelTitle, String viewCount, String thumbnailUrl, String createdAt, String updatedAt) {
		this.videoId = videoId;
		this.title = title;
		this.channelTitle = channelTitle;
		this.viewCount = viewCount;
		this.thumbnailUrl = thumbnailUrl;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}
}
