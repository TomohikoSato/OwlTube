package com.example.tomohiko_sato.mytube.presentation;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.presentation.TopFragment.OnTopFragmentInteractionListener;

import java.util.List;

import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Item;
import com.squareup.picasso.Picasso;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item} and makes a call to the
 * specified {@link OnTopFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

	private List<Item> items;
	private final OnTopFragmentInteractionListener listener;
	private Context context;

	public MyItemRecyclerViewAdapter(List<Item> items, OnTopFragmentInteractionListener listener, Context context) {
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
		holder.title.setText(items.get(position).snippet.title);
		holder.channelTitle.setText(items.get(position).snippet.channelTitle);
		holder.viewCount.setText("0");
		Picasso.with(context).load(items.get(position).snippet.thumbnails.medium.url).into(holder.thumbnail);

		/*holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
				}
				listener.onTopFragmentInteraction(holder.mItem);
			}
		});*/
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		TextView title;
		TextView channelTitle;
		TextView viewCount;
		ImageView thumbnail;

		public ViewHolder(View view) {
			super(view);
			title = (TextView) view.findViewById(R.id.title);
			channelTitle = (TextView) view.findViewById(R.id.channel_title);
			viewCount = (TextView) view.findViewById(R.id.view_count);
			thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
		}
	}
}
