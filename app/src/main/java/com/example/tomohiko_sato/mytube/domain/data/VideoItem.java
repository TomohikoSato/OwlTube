package com.example.tomohiko_sato.mytube.domain.data;

import android.os.Parcel;
import android.os.Parcelable;

public class VideoItem implements Parcelable {
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

	protected VideoItem(Parcel in) {
		videoId = in.readString();
		title = in.readString();
		channelTitle = in.readString();
		viewCount = in.readString();
		thumbnailUrl = in.readString();
	}

	public static final Creator<VideoItem> CREATOR = new Creator<VideoItem>() {
		@Override
		public VideoItem createFromParcel(Parcel in) {
			return new VideoItem(in);
		}

		@Override
		public VideoItem[] newArray(int size) {
			return new VideoItem[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(videoId);
		dest.writeString(title);
		dest.writeString(channelTitle);
		dest.writeString(viewCount);
		dest.writeString(thumbnailUrl);
	}
}
