package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VideoItem}.
 * {@link VideoItem} tap event call {@link OnVideoItemSelectedListener}.
 */
public class VideoItemRecyclerViewAdapter extends RecyclerView.Adapter<VideoItemRecyclerViewAdapter.ViewHolder> {

	public interface OnVideoItemSelectedListener {
		void onVideoItemSelected(VideoItem item);
	}

	private List<VideoItem> items;
	private final OnVideoItemSelectedListener listener;
	private Context context;

	public VideoItemRecyclerViewAdapter(List<VideoItem> items, OnVideoItemSelectedListener listener, Context context) {
		this.items = items;
		this.listener = listener;
		this.context = context;
	}

	public void setItems(List<VideoItem> items) {
		this.items = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_video, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		VideoItem item = items.get(position);
		holder.item = item;
		holder.title.setText(item.title);
		holder.channelTitle.setText(item.channelTitle);
		holder.viewCount.setText(StringUtil.convertDisplayViewCount(item.viewCount));
		Picasso.with(context).load(item.thumbnailUrl).into(holder.thumbnail);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener == null || holder.item == null) {
					return;
				}
				listener.onVideoItemSelected(holder.item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		VideoItem item;
		View itemView;
		TextView title;
		TextView channelTitle;
		TextView viewCount;
		ImageView thumbnail;

		ViewHolder(View view) {
			super(view);
			itemView = view;
			title = (TextView) view.findViewById(R.id.title);
			channelTitle = (TextView) view.findViewById(R.id.channel_title);
			viewCount = (TextView) view.findViewById(R.id.view_count);
			thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		}
	}
}
