package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;

public class VideoItemRecyclerViewHolder extends RecyclerView.ViewHolder {
	VideoItem item;
	View itemView;
	TextView title;
	TextView channelTitle;
	TextView viewCount;
	ImageView thumbnail;

	VideoItemRecyclerViewHolder(View view) {
		super(view);
		itemView = view;
		title = (TextView) view.findViewById(R.id.title);
		channelTitle = (TextView) view.findViewById(R.id.channel_title);
		viewCount = (TextView) view.findViewById(R.id.view_count);
		thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
	}
}
