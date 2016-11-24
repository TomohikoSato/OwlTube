package com.example.tomohiko_sato.owltube.domain.data;

import android.os.Parcel;
import android.os.Parcelable;

public class Video implements Parcelable {
	public String videoId;
	public String title;
	public String channelTitle;
	public String viewCount;
	public String thumbnailUrl;

	public Video(String videoId, String title, String channelTitle, String viewCount, String thumbnailUrl) {
		this.videoId = videoId;
		this.title = title;
		this.channelTitle = channelTitle;
		this.viewCount = viewCount;
		this.thumbnailUrl = thumbnailUrl;
	}

	protected Video(Parcel in) {
		videoId = in.readString();
		title = in.readString();
		channelTitle = in.readString();
		viewCount = in.readString();
		thumbnailUrl = in.readString();
	}

	public static final Creator<Video> CREATOR = new Creator<Video>() {
		@Override
		public Video createFromParcel(Parcel in) {
			return new Video(in);
		}

		@Override
		public Video[] newArray(int size) {
			return new Video[size];
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
