package com.example.tomohiko_sato.mytube.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSearchResultFragmentInteractionListener}
 * interface.
 */
public class SearchResultFragment extends Fragment {
	private OnSearchResultFragmentInteractionListener listener;

	private final static String KEY_VIDEO_ITEMS = "VIDEO_ITEMS";
	private SearchResultRecyclerViewAdapter adapter;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchResultFragment() {
	}

	public static SearchResultFragment newInstance(@Nullable List<VideoItem> items) {
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
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_searchresult_list, container, false);
		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		Bundle bundle = getArguments();
		List<VideoItem> items = new ArrayList<>();
		if (bundle != null) {
			items = bundle.getParcelableArrayList(KEY_VIDEO_ITEMS);
		}

		adapter = new SearchResultRecyclerViewAdapter(items, listener, context);
		recyclerView.setAdapter(adapter);

		return recyclerView;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnSearchResultFragmentInteractionListener) {
			listener = (OnSearchResultFragmentInteractionListener) context;
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

	public void setVideoItems(List<VideoItem> videoItems) {
		adapter.setItems(videoItems);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 * <p/>
	 * See the Android Training lesson <a href=
	 * "http://developer.android.com/training/basics/fragments/communicating.html"
	 * >Communicating with Other Fragments</a> for more information.
	 */
	public interface OnSearchResultFragmentInteractionListener {
		// TODO: Update argument type and name
		void onSearchResultFragmentInteraction(VideoItem item);
	}
}
