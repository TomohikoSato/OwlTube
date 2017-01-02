package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewHolder;
import com.example.tomohiko_sato.owltube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

class PlayerRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
	interface OnVideoItemSelectedListener {
		void onVideoItemSelected(Video item);
	}

	private enum ViewType {
		Player(0, R.layout.list_item_video_detail),
		RelatedVideos(1, R.layout.list_item_video);

		public final int viewTypeInt;
		public final int layoutId;

		ViewType(final int viewType, @LayoutRes final int layoutId) {
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
			if (this == ViewType.Player) {
				return new VideoDetailRecyclerViewHolder(view);
			} else {
				return new VideoItemRecyclerViewHolder(view);
			}
		}
	}

	private final Video headerItem;
	private final List<Video> bodyItems = new ArrayList<>();
	private final Context context;
	private final OnVideoItemSelectedListener listener;

	PlayerRecyclerViewAdapter(Context context, OnVideoItemSelectedListener listener, Video headerItem) {
		this.context = context;
		this.listener = listener;
		this.headerItem = headerItem;
	}

	void setBodyItem(List<Video> items) {
		this.bodyItems.clear();
		this.bodyItems.addAll(items);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
		ViewType viewType = ViewType.fromInt(viewTypeInt);
		return viewType.getViewHolder(viewType.getView(parent));
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 0) {
			return ViewType.Player.viewTypeInt;
		}
		return ViewType.RelatedVideos.viewTypeInt;
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		ViewType viewType = ViewType.fromInt(getItemViewType(position));
		if (viewType == ViewType.Player) {
			VideoDetailRecyclerViewHolder vdvh = (VideoDetailRecyclerViewHolder) holder;
			vdvh.item = headerItem;
			vdvh.title.setText(headerItem.title);
			vdvh.channelTitle.setText(headerItem.channelTitle);
			vdvh.viewCount.setText(StringUtil.convertDisplayViewCount(headerItem.viewCount));
		} else if (viewType == ViewType.RelatedVideos) {
			final VideoItemRecyclerViewHolder vivh = (VideoItemRecyclerViewHolder) holder;

			int bodyPosition = position - 1;

			vivh.item = bodyItems.get(bodyPosition);
			vivh.title.setText(bodyItems.get(bodyPosition).title);
			vivh.channelTitle.setText(bodyItems.get(bodyPosition).channelTitle);
			vivh.viewCount.setText(StringUtil.convertDisplayViewCount(bodyItems.get(bodyPosition).viewCount));
			Picasso.with(context).load(bodyItems.get(bodyPosition).thumbnailUrl).into(vivh.thumbnail);

			vivh.itemView.setOnClickListener(v -> {
				if (listener == null || vivh.item == null) {
					return;
				}
				listener.onVideoItemSelected(vivh.item);
			});
		}
	}

	@Override
	public int getItemCount() {
		return bodyItems.size() + (headerItem == null ? 0 : 1);
	}
}
