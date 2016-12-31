package com.example.tomohiko_sato.owltube.presentation.top.popular;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.OnPagingScrollListener;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * 人気な動画の一覧が観れるFragment.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnVideoItemSelectedListener}
 * interface.
 * </p>
 */
public class PopularFragment extends Fragment {
	private final static String TAG = PopularFragment.class.getSimpleName();
	private OnVideoItemSelectedListener listener;
	VideoItemRecyclerViewAdapter adapter;

	@Inject
	PopularUseCase popularUC;

	public static PopularFragment newInstance() {
		return new PopularFragment();
	}

	public PopularFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		((OwlTubeApp) context.getApplicationContext()).getComponent().inject(this);
		if (context instanceof OnVideoItemSelectedListener) {
			listener = (OnVideoItemSelectedListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnPopularFragmentInteractionListener to attached activity");
		}
	}

	String nextPageToken;

	final OnPagingScrollListener scrollListener = new OnPagingScrollListener(10, new OnPagingScrollListener.OnShouldLoadNextPageListener() {
		@Override
		public void onShouldLoadNextPage(int lastItemPosition) {
			Log.d(TAG, "paging. nextPageToken: " + nextPageToken);
			if (nextPageToken != null) {
				popularUC.fetchPopular(nextPageToken, fetchCallback);
			}
		}
	});

	// TODO: Callbackが呼ばれてる頃にはFragmentがDestroyされていることがある。それによってメモリリークや予期せぬ不具合が起きることがある。
	// RxやEventBusを導入して修正したい
	final Callback<VideoResponse> fetchCallback = new Callback<VideoResponse>() {
		@Override
		public void onSuccess(VideoResponse response) {
			Log.d(TAG, "onSuccess");
			adapter.addItems(response.videos);
			nextPageToken = response.pageToken;
			progressBar.setVisibility(View.GONE);
			scrollListener.onLoadCompleted();
		}

		@Override
		public void onFailure(Throwable t) {
			Toast.makeText(getContext(), "データの読み込みに失敗しました", Toast.LENGTH_LONG).show();
			progressBar.setVisibility(View.GONE);
		}
	};

	ProgressBar progressBar;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(adapter = new VideoItemRecyclerViewAdapter(new ArrayList<Video>(), listener, getContext()));
		recyclerView.addOnScrollListener(scrollListener);
		popularUC.fetchPopular(fetchCallback);

		return rootView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Log.d(TAG, "onDetach");
		listener = null;
	}
}