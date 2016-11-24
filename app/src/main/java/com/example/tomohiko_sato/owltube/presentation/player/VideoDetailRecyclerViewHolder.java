package com.example.tomohiko_sato.owltube.presentation.player;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;

public class VideoDetailRecyclerViewHolder extends RecyclerView.ViewHolder {
	Video item;
	View itemView;
	TextView title;
	TextView channelTitle;
	TextView viewCount;

	public VideoDetailRecyclerViewHolder(View view) {
		super(view);
		itemView = view;
		title = (TextView) view.findViewById(R.id.title);
		channelTitle = (TextView) view.findViewById(R.id.channel_title);
		viewCount = (TextView) view.findViewById(R.id.view_count);
	}
}
