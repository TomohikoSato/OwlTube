package com.example.tomohiko_sato.owltube.presentation.top.recently_watched;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter.OnVideoItemSelectedListener;
import com.example.tomohiko_sato.owltube.common.util.Logger;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnVideoItemSelectedListener}
 * interface.
 */
public class RecentlyWatchedFragment extends Fragment {
	private OnVideoItemSelectedListener listener;
	@Inject
	RecentlyWatchedUseCase recentlyWatchedUC;

	private final CompositeDisposable disposables = new CompositeDisposable();

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
	private VideoItemViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recently_watched, container, false);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

		adapter = new VideoItemViewAdapter(items, listener, getContext());
		recyclerView.setAdapter(adapter);
		refreshItem();

		return recyclerView;
	}

	public void refreshItem() {
		disposables.add(recentlyWatchedUC.fetchRecentlyWatched()
				.subscribe((videos) -> {
					items.clear();
					items.addAll(videos);
					adapter.notifyDataSetChanged();
				}, t -> {
					t.printStackTrace();
					Logger.e("fetch RecentlyWatched onFailure");
				}));
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((OwlTubeApp) context.getApplicationContext()).getComponent().inject(this);
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
		disposables.dispose();
	}
}