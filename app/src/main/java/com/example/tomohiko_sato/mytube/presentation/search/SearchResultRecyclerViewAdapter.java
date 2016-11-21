package com.example.tomohiko_sato.mytube.presentation.search;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.presentation.search.SearchResultFragment.OnSearchResultFragmentInteractionListener;
import com.example.tomohiko_sato.mytube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link VideoItem} and makes a call to the
 * specified {@link OnSearchResultFragmentInteractionListener}.
 */
public class SearchResultRecyclerViewAdapter extends RecyclerView.Adapter<SearchResultRecyclerViewAdapter.ViewHolder> {

	private List<VideoItem> items;
	private final OnSearchResultFragmentInteractionListener listener;
	private final Context context;

	public SearchResultRecyclerViewAdapter(List<VideoItem> items, OnSearchResultFragmentInteractionListener listener, Context context) {
		this.items = items;
		this.listener = listener;
		this.context = context;
	}

	public void setItems(List<VideoItem> items) {
		this.items = items;
		notifyDataSetChanged();
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.video_list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {		VideoItem item = items.get(position);
		holder.item = item;
		holder.title.setText(item.title);
		holder.channelTitle.setText(item.channelTitle);
		holder.viewCount.setText(StringUtil.convertDisplayViewCount(item.viewCount));
		Picasso.with(context).load(item.thumbnailUrl).into(holder.thumbnail);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					listener.onSearchResultFragmentInteraction(holder.item);
				}
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
