package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewHolder;
import com.example.tomohiko_sato.owltube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PlayerRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
	public interface OnVideoItemSelectedListener {
		void onVideoItemSelected(VideoItem item);
	}

	enum ViewType {
		Header(0, R.layout.list_item_video_detail),
		Body(1, R.layout.list_item_video);

		public final int viewTypeInt;
		public final int layoutId;

		private ViewType(final int viewType, @LayoutRes final int layoutId) {
			this.viewTypeInt = viewType;
			this.layoutId = layoutId;
		}

		public static ViewType fromInt(int viewType) {
			for (ViewType value : values()) {
				if (value.viewTypeInt == viewType) {
					return value;
				}
			}

			throw new IllegalArgumentException();
		}

		public View getView(ViewGroup parent) {
			return LayoutInflater.from(parent.getContext())
					.inflate(layoutId, parent, false);
		}

		public ViewHolder getViewHolder(View view) {
			if (this == ViewType.Header) {
				return new VideoDetailRecyclerViewHolder(view);
			} else {
				return new VideoItemRecyclerViewHolder(view);
			}
		}
	}

	private final VideoItem headerItem;
	List<VideoItem> bodyItems = new ArrayList<>();
	private final Context context;
	private final OnVideoItemSelectedListener listener;

	public PlayerRecyclerViewAdapter(Context context, OnVideoItemSelectedListener listener, VideoItem headerItem) {
		this.context = context;
		this.listener = listener;
		this.headerItem = headerItem;
	}

	public void setBodyItem(List<VideoItem> items) {
		this.bodyItems = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
		ViewType viewType = ViewType.fromInt(viewTypeInt);
		View view = viewType.getView(parent);
		return viewType.getViewHolder(view);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ViewType.Header.viewTypeInt;
		}
		return ViewType.Body.viewTypeInt;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		ViewType viewType = ViewType.fromInt(getItemViewType(position));
		if (viewType == ViewType.Header) {
			VideoDetailRecyclerViewHolder vdvh = (VideoDetailRecyclerViewHolder) holder;
			vdvh.item = headerItem;
			vdvh.title.setText(headerItem.title);
			vdvh.channelTitle.setText(headerItem.channelTitle);
			vdvh.viewCount.setText(StringUtil.convertDisplayViewCount(headerItem.viewCount));
		} else if (viewType == ViewType.Body) {
			final VideoItemRecyclerViewHolder vivh = (VideoItemRecyclerViewHolder) holder;
			vivh.item = headerItem;
			vivh.title.setText(headerItem.title);
			vivh.channelTitle.setText(headerItem.channelTitle);
			vivh.viewCount.setText(StringUtil.convertDisplayViewCount(headerItem.viewCount));
			Picasso.with(context).load(headerItem.thumbnailUrl).into(vivh.thumbnail);

			vivh.itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (listener == null || vivh.item == null) {
						return;
					}
					listener.onVideoItemSelected(vivh.item);
				}
			});
		}

	}

	@Override
	public int getItemCount() {
		return bodyItems.size() + (headerItem == null ? 0 : 1);
	}
}
