package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Video}.
 * {@link Video} tap event call {@link OnVideoItemSelectedListener}.
 */
public class VideoItemViewAdapter extends RecyclerView.Adapter<VideoItemViewHolder> {
	public interface OnVideoItemSelectedListener {
		void onVideoItemSelected(Video item);
	}

	private List<Video> items;
	private final OnVideoItemSelectedListener listener;
	private Context context;

	public VideoItemViewAdapter(@NonNull List<Video> items, @NonNull OnVideoItemSelectedListener listener, @NonNull Context context) {
		this.items = items;
		this.listener = listener;
		this.context = context;
	}

	public void addItems(@NonNull List<Video> addItems) {
		this.items.addAll(addItems);
		notifyItemRangeInserted(items.size() - addItems.size() + 1, addItems.size());
	}

	@Override
	public VideoItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_video, parent, false);
		return new VideoItemViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final VideoItemViewHolder holder, int position) {
		Video item = items.get(position);
		holder.item = item;
		holder.title.setText(item.title);
		holder.channelTitle.setText(item.channelTitle);
		holder.viewCount.setText(StringUtil.convertDisplayViewCount(item.viewCount));
		Picasso.with(context).load(item.thumbnailUrl).into(holder.thumbnail);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (holder.item == null) {
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
}
