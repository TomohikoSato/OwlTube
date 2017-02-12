package com.example.tomohiko_sato.owltube.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.presentation.common_component.OnPagingScrollListener;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter.OnVideoItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnVideoItemSelectedListener}
 * interface.
 */
public class SearchResultFragment extends Fragment implements OnVideoItemSelectedListener {
	private SearchResultFragmentInteractionListener listener;

	private final static String KEY_VIDEO_ITEMS = "VIDEO_ITEMS";
	private VideoItemViewAdapter adapter;
	private ProgressBar progressBar;

	@Override
	public void onVideoItemSelected(Video item) {
		listener.onVideoItemSelected(item);
	}

	interface SearchResultFragmentInteractionListener {
		void onLoadMore();

		void onVideoItemSelected(Video item);
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchResultFragment() {
	}

	public static SearchResultFragment newInstance(@Nullable List<Video> items) {
		SearchResultFragment fragment = new SearchResultFragment();
		if (items != null) {
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList(KEY_VIDEO_ITEMS, new ArrayList(items));
			fragment.setArguments(bundle);
		}
		return fragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final Context context = getContext();
		View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);

		progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);
		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.related_videos);
		recyclerView.addOnScrollListener(scrollListener);
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		Bundle bundle = getArguments();
		List<Video> items = new ArrayList<>();
		if (bundle != null) {
			progressBar.setVisibility(View.GONE);
			items = bundle.getParcelableArrayList(KEY_VIDEO_ITEMS);
		}

		adapter = new VideoItemViewAdapter(items, this, context);
		recyclerView.setAdapter(adapter);

		return rootView;
	}

	final OnPagingScrollListener scrollListener = new OnPagingScrollListener(new OnPagingScrollListener.OnShouldLoadNextPageListener() {
		@Override
		public void onShouldLoadNextPage(int lastItemPosition) {
			listener.onLoadMore();
		}
	});

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof SearchResultFragmentInteractionListener) {
			listener = (SearchResultFragmentInteractionListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnListFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public void addVideoItems(List<Video> videos) {
		progressBar.setVisibility(View.GONE);
		scrollListener.onLoadCompleted();
		adapter.addItems(videos);
	}
}
