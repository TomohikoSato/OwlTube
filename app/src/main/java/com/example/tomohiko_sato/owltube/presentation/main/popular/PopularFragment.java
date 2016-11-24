package com.example.tomohiko_sato.owltube.presentation.main.popular;

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

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.owltube.di.SampleModule;
import com.example.tomohiko_sato.owltube.domain.data.Video;
import com.example.tomohiko_sato.owltube.domain.data.VideoResponse;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.domain.callback.Callback;
import com.example.tomohiko_sato.owltube.presentation.common_component.OnPagingScrollListener;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

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
		PopularFragment fragment = new PopularFragment();
		return fragment;
	}

	public PopularFragment() {
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		DaggerSampleComponent.builder().sampleModule(new SampleModule(context)).build().inject(this);
		if (context instanceof OnVideoItemSelectedListener) {
			listener = (OnVideoItemSelectedListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnPopularFragmentInteractionListener to attached activity");
		}
	}

	String nextPageToken;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		final Context context = getContext();

		View rootView = inflater.inflate(R.layout.fragment_popular, container, false);
		RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
		final ProgressBar progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar);

		final LinearLayoutManager llm = new LinearLayoutManager(context);
		recyclerView.setLayoutManager(llm);

		final Callback<VideoResponse> fetchCallback = new Callback<VideoResponse>() {
			@Override
			public void onSuccess(VideoResponse response) {
				adapter.addItems(response.videos);
				nextPageToken = response.pageToken;
				progressBar.setVisibility(View.GONE);
			}

			@Override
			public void onFailure(Throwable t) {
				Toast.makeText(context, "データの読み込みに失敗しました", Toast.LENGTH_LONG).show();
				progressBar.setVisibility(View.GONE);
			}
		};

		adapter = new VideoItemRecyclerViewAdapter(new ArrayList<Video>(), listener, context);
		recyclerView.setAdapter(adapter);
		recyclerView.addOnScrollListener(new OnPagingScrollListener(10, new OnPagingScrollListener.OnShouldLoadNextPageListener() {
			@Override
			public void onShouldLoadNextPage(int lastItemPosition) {
				//TODO: ページング処理
				Log.d(TAG, "paging...");
				popularUC.fetchPopular(nextPageToken, fetchCallback);

			}
		}));
		popularUC.fetchPopular(fetchCallback);

		return rootView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}
}