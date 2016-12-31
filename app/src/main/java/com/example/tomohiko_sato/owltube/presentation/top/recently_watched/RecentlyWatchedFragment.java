package com.example.tomohiko_sato.owltube.presentation.top.recently_watched;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnVideoItemSelectedListener}
 * interface.
 */
public class RecentlyWatchedFragment extends Fragment {
	private static final String TAG = RecentlyWatchedFragment.class.getSimpleName();
	private OnVideoItemSelectedListener listener;
	@Inject
	RecentlyWatchedUseCase recentlyWatchedUC;

	public RecentlyWatchedFragment() {
	}

	public static RecentlyWatchedFragment newInstance() {
		return new RecentlyWatchedFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	private final List<Video> items = new ArrayList<>();
	private VideoItemRecyclerViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recently_watched, container, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		adapter = new VideoItemRecyclerViewAdapter(items, listener, getContext());
		recyclerView.setAdapter(adapter);
		refreshItem();

		return recyclerView;
	}

	public void refreshItem() {
		recentlyWatchedUC.fetchRecentlyWatched(new Callback<List<Video>>() {
			@Override
			public void onSuccess(List<Video> response) {
				items.clear();
				items.addAll(response);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				Log.d(TAG, "fetch RecentlyWatched onFailure");
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((OwlTubeApp)context.getApplicationContext()).getComponent().inject(this);
		if (context instanceof OnVideoItemSelectedListener) {
			listener = (OnVideoItemSelectedListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnRecentlyWatchedFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
}