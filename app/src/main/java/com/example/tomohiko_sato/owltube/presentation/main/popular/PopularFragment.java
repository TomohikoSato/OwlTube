package com.example.tomohiko_sato.owltube.presentation.main.popular;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.owltube.di.SampleModule;
import com.example.tomohiko_sato.owltube.domain.data.VideoItem;
import com.example.tomohiko_sato.owltube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.owltube.domain.util.Callback;
import com.example.tomohiko_sato.owltube.presentation.common_component.VideoItemRecyclerViewAdapter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * 人気な動画の一覧が観れるFragment.
 * <p>
 * Activities containing this fragment MUST implement the {@link OnPopularFragmentInteractionListener}
 * interface.
 * </p>
 */
public class PopularFragment extends Fragment implements VideoItemRecyclerViewAdapter.OnVideoItemSelectedListener {
	private final static String TAG = PopularFragment.class.getSimpleName();
	private OnPopularFragmentInteractionListener listener;
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
		if (context instanceof OnPopularFragmentInteractionListener) {
			listener = (OnPopularFragmentInteractionListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnPopularFragmentInteractionListener to attached activity");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_popular, container, false);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		adapter = new VideoItemRecyclerViewAdapter(new ArrayList<VideoItem>(), this, context);
		recyclerView.setAdapter(adapter);


		popularUC.fetchPopular(new Callback<List<VideoItem>>() {
			@Override
			public void onSuccess(List<VideoItem> items) {
				adapter.setItems(items);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {
			}
		});

		return recyclerView;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	@Override
	public void onVideoItemSelected(VideoItem item) {
		listener.onPopularFragmentInteraction(item);
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 */
	public interface OnPopularFragmentInteractionListener {
		void onPopularFragmentInteraction(VideoItem item);
	}
}