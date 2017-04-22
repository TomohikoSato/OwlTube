package com.example.tomohiko_sato.owltube.presentation.top.popular;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.OwlTubeApp;
import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.common.util.Logger;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.presentation.common_component.OnPagingScrollListener;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemViewAdapter.OnVideoItemSelectedListener;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;

/**
 * 人気な動画の一覧が観れるFragment.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnVideoItemSelectedListener}
 * interface.
 * </p>
 */
public class PopularFragment extends Fragment {
	public static final String TAG = PopularFragment.class.getSimpleName();

	private final CompositeDisposable disposables = new CompositeDisposable();
	private OnVideoItemSelectedListener listener;
	private VideoItemViewAdapter adapter;
	private String nextPageToken;
	private ProgressBar progressBar;
	private final OnPagingScrollListener scrollListener = new OnPagingScrollListener(new OnPagingScrollListener.OnShouldLoadNextPageListener() {
		@Override
		public void onShouldLoadNextPage(int lastItemPosition) {
			Logger.d("paging. nextPageToken: " + nextPageToken);
			if (nextPageToken != null) {
				disposables.add(popularUC
						.fetchNextPopular(nextPageToken)
						.observeOn(AndroidSchedulers.mainThread())
						.subscribeWith(new VideoResponseObserver(PopularFragment.this)));
			}
		}
	});

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.related_videos);
		recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
		recyclerView.setAdapter(adapter = new VideoItemViewAdapter(new ArrayList<>(), listener, getContext()));
		recyclerView.addOnScrollListener(scrollListener);
		disposables.add(popularUC.fetchPopular()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribeWith(new VideoResponseObserver(PopularFragment.this)));

		return rootView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		Logger.d("onDetach");
		disposables.dispose();
		listener = null;
	}

	static class VideoResponseObserver extends DisposableSingleObserver<VideoResponse> {
		private PopularFragment f;

		VideoResponseObserver(PopularFragment f) {
			this.f = f;
		}

		@Override
		public void onSuccess(VideoResponse response) {
			f.adapter.addItems(response.videos);
			f.nextPageToken = response.pageToken;
			f.progressBar.setVisibility(View.GONE);
			f.scrollListener.onLoadCompleted();
		}

		@Override
		public void onError(Throwable t) {
			Toast.makeText(f.getContext(), "データの読み込みに失敗しました", Toast.LENGTH_LONG).show();
			f.progressBar.setVisibility(View.GONE);
		}
	}
}