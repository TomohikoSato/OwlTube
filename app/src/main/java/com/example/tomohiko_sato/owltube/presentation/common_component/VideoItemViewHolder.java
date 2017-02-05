package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;

import static java.util.Objects.requireNonNull;

public class VideoItemViewHolder extends RecyclerView.ViewHolder {
	public Video item;
	@NonNull
	public final View itemView;
	@NonNull
	public final TextView title;
	@NonNull
	public final TextView channelTitle;
	@NonNull
	public final TextView viewCount;
	@NonNull
	public final ImageView thumbnail;

	public VideoItemViewHolder(@NonNull View view) {
		super(view);
		itemView = view;
		title = requireNonNull((TextView) view.findViewById(R.id.title));
		channelTitle = requireNonNull((TextView) view.findViewById(R.id.channel_title));
		viewCount = requireNonNull((TextView) view.findViewById(R.id.view_count));
		thumbnail = requireNonNull((ImageView) view.findViewById(R.id.thumbnail));
	}
}
