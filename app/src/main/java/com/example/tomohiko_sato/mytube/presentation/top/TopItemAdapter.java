package com.example.tomohiko_sato.mytube.presentation.top;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Item;
import com.example.tomohiko_sato.mytube.presentation.top.TopFragment.OnTopFragmentInteractionListener;
import com.example.tomohiko_sato.mytube.presentation.util.StringUtil;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item} and makes a call to the
 * specified {@link OnTopFragmentInteractionListener}.
 */
public class TopItemAdapter extends RecyclerView.Adapter<TopItemAdapter.ViewHolder> {

	private List<Item> items;
	private final OnTopFragmentInteractionListener listener;
	private Context context;

	public TopItemAdapter(List<Item> items, OnTopFragmentInteractionListener listener, Context context) {
		this.items = items;
		this.listener = listener;
		this.context = context;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.search_result_list_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.item = items.get(position);
		holder.title.setText(items.get(position).snippet.title);
		holder.channelTitle.setText(items.get(position).snippet.channelTitle);
		holder.viewCount.setText(StringUtil.convertDisplayViewCount(items.get(position).statistics.viewCount));
		Picasso.with(context).load(items.get(position).snippet.thumbnails.medium.url).into(holder.thumbnail);

		holder.itemView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (listener == null || holder.item == null) {
					return;
				}
				listener.onTopFragmentInteraction(holder.item);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	static class ViewHolder extends RecyclerView.ViewHolder {
		Item item;
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
