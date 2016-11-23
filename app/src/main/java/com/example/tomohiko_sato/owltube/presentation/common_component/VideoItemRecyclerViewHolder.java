package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;

public class VideoItemRecyclerViewHolder extends RecyclerView.ViewHolder {
	public VideoItem item;
	public View itemView;
	public TextView title;
	public TextView channelTitle;
	public TextView viewCount;
	public ImageView thumbnail;

	public VideoItemRecyclerViewHolder(View view) {
		super(view);
		itemView = view;
		title = (TextView) view.findViewById(R.id.title);
		channelTitle = (TextView) view.findViewById(R.id.channel_title);
		viewCount = (TextView) view.findViewById(R.id.view_count);
		thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
	}
}
