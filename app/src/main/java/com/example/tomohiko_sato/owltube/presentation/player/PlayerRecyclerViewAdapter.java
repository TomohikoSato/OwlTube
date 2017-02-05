package com.example.tomohiko_sato.owltube.presentation.player;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
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
		@LayoutRes
		public final int layoutId;

		ViewType(final int viewType, @LayoutRes final int layoutId) {
			this.viewTypeInt = viewType;
			this.layoutId = layoutId;
		}

		public static ViewType from(int viewType) {
			for (ViewType value : values()) {
				if (value.viewTypeInt == viewType) {
					return value;
				}
			}

			throw new IllegalArgumentException();
		}

		public ViewHolder getViewHolder(ViewGroup parent) {
			if (this == ViewType.Player) {
				return new VideoDetailRecyclerViewHolder(getView(parent));
			} else {
				return new VideoItemRecyclerViewHolder(getView(parent));
			}
		}

		private View getView(ViewGroup parent) {
			return LayoutInflater.from(parent.getContext())
					.inflate(layoutId, parent, false);
		}
	}

	@NonNull
	private final Video headerItem;
	@NonNull
	private final List<Video> bodyItems;
	@NonNull
	private final Context context;
	@NonNull
	private final OnVideoItemSelectedListener listener;

	PlayerRecyclerViewAdapter(@NonNull Context context, @NonNull OnVideoItemSelectedListener listener, @NonNull Video headerItem, @NonNull List<Video> bodyItems) {
		this.context = context;
		this.listener = listener;
		this.headerItem = headerItem;
		this.bodyItems = new ArrayList<>(bodyItems);
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewTypeInt) {
		return ViewType.from(viewTypeInt).getViewHolder(parent);
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
		ViewType viewType = ViewType.from(getItemViewType(position));
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
				if (vivh.item == null) {
					return;
				}
				listener.onVideoItemSelected(vivh.item);
			});
		}
	}

	@Override
	public int getItemCount() {
		return bodyItems.size() + 1;
	}
}
