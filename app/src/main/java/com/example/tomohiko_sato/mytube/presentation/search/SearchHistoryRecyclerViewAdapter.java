package com.example.tomohiko_sato.mytube.presentation.search;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.presentation.search.SearchHistoryFragment.OnSearchHistoryFragmentInteractionListener;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link String} and makes a call to the
 * specified {@link OnSearchHistoryFragmentInteractionListener}.
  */
public class SearchHistoryRecyclerViewAdapter extends RecyclerView.Adapter<SearchHistoryRecyclerViewAdapter.ViewHolder> {

	private final List<String> searchHistories;
	private final OnSearchHistoryFragmentInteractionListener listener;

	public SearchHistoryRecyclerViewAdapter(List<String> searchHistories, OnSearchHistoryFragmentInteractionListener listener) {
		this.searchHistories = searchHistories;
		this.listener = listener;
	}

	@Override
	public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = LayoutInflater.from(parent.getContext())
				.inflate(R.layout.fragment_item, parent, false);
		return new ViewHolder(view);
	}

	@Override
	public void onBindViewHolder(final ViewHolder holder, int position) {
		holder.searchHistory = searchHistories.get(position);
		holder.searchHistoryTextView.setText(searchHistories.get(position));

		holder.view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (null != listener) {
					// Notify the active callbacks interface (the activity, if the
					// fragment is attached to one) that an item has been selected.
					listener.OnSearchHistoryFragmentInteraction(holder.searchHistory);
				}
			}
		});
	}

	@Override
	public int getItemCount() {
		return searchHistories.size();
	}

	public class ViewHolder extends RecyclerView.ViewHolder {
		public final View view;
		public final TextView searchHistoryTextView;
		public String searchHistory;

		public ViewHolder(View view) {
			super(view);
			this.view = view;
			searchHistoryTextView = (TextView) view.findViewById(R.id.id);
		}
	}
}
