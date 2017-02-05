package com.example.tomohiko_sato.owltube.presentation.player;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;

import static java.util.Objects.requireNonNull;

class VideoDetailRecyclerViewHolder extends RecyclerView.ViewHolder {
	@Nullable
	Video item;
	@NonNull
	final TextView title;
	@NonNull
	final TextView channelTitle;
	@NonNull
	final TextView viewCount;

	VideoDetailRecyclerViewHolder(@NonNull View view) {
		super(view);
		title = requireNonNull((TextView) view.findViewById(R.id.title));
		channelTitle = requireNonNull((TextView) view.findViewById(R.id.channel_title));
		viewCount = requireNonNull((TextView) view.findViewById(R.id.view_count));
	}
}
