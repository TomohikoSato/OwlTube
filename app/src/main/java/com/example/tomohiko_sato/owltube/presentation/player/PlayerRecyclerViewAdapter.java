package com.example.tomohiko_sato.owltube.presentation.player;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewHolder;

public class PlayerRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
	enum ViewType {
		Header(0),
		Body(1),
		Footer(2);

		private final int viewType;
		private ViewType(final int viewType) {
			this.viewType = viewType;
		}
		public int getViewType() {
			return this.viewType;
		}
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_video, parent, false);
		return new VideoItemRecyclerViewHolder(view);
	}

	@Override
	public int getItemViewType(int position) {
		if (position == 1) {
			return ViewType.Header.getViewType();
		}
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}
}
