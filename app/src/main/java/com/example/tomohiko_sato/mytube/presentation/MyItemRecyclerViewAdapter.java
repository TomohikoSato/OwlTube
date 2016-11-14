package com.example.tomohiko_sato.mytube.presentation;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.presentation.TopFragment.OnTopFragmentInteractionListener;

import java.util.List;

import com.example.tomohiko_sato.mytube.api.youtube.data.Item;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Item} and makes a call to the
 * specified {@link OnTopFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyItemRecyclerViewAdapter extends RecyclerView.Adapter<MyItemRecyclerViewAdapter.ViewHolder> {

	private List<Item> items;
	private final OnTopFragmentInteractionListener listener;

	public MyItemRecyclerViewAdapter(List<Item> items, OnTopFragmentInteractionListener listener) {
		this.items = items;
		this.listener = listener;
	}

	public void setItems(List<Item> items) {
		this.items = items;
	}


	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.mItem = items.get(position);
		holder.mIdView.setText(items.get(position).id.videoId);
		holder.mContentView.setText(items.get(position).kind);

		holder.mView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
				}
				listener.onTopFragmentInteraction(holder.mItem);
			}
		});
	}

	@Override
	public int getItemCount() {
		return items.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View mView;
		public final TextView mIdView;
		public final TextView mContentView;
		public Item mItem;

		public ViewHolder(View view) {
			super(view);
			mView = view;
			mIdView = (TextView) view.findViewById(R.id.id);
			mContentView = (TextView) view.findViewById(R.id.content);
		}

		@Override
		public String toString() {
			return super.toString() + " '" + mContentView.getText() + "'";
		}
	}
}
