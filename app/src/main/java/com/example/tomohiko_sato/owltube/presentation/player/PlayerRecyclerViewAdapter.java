package com.example.tomohiko_sato.owltube.presentation.player;

import android.support.annotation.IntDef;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewHolder;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PlayerRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

	private static final int VIEW_TYPE_HEADER = 0;
	private static final int VIEW_TYPE_BODY = 1;
	private static final int VIEW_TYPE_FOOTER = 2;

	@IntDef(value = {VIEW_TYPE_HEADER, VIEW_TYPE_BODY, VIEW_TYPE_FOOTER})
	@Retention(RetentionPolicy.SOURCE)
	@interface ITEM_VIEW_TYPE {
	}


	@Override
	public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ITEM_VIEW_TYPE int viewType) {
		
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.list_item_video, parent, false);
		return new VideoItemRecyclerViewHolder(view);
	}

	@Override
	@ITEM_VIEW_TYPE
	public int getItemViewType(int position) {
		if (position == 1) {
			return VIEW_TYPE_HEADER;
		}
		return VIEW_TYPE_BODY;
	}

	@Override
	public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

	}

	@Override
	public int getItemCount() {
		return 0;
	}
}
