package com.example.tomohiko_sato.mytube.presentation.main.recently_watched;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.mytube.di.SampleModule;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.recently_watched.RecentlyWatchedUseCase;
import com.example.tomohiko_sato.mytube.domain.util.Callback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRecentlyWatchedFragmentInteractionListener}
 * interface.
 */
public class RecentlyWatchedFragment extends Fragment {
	private static final String TAG = RecentlyWatchedFragment.class.getSimpleName();
	private OnRecentlyWatchedFragmentInteractionListener listener;
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

	List<VideoItem> items = new ArrayList<>();
	RecentlyWatchedRecyclerViewAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_recentlywatched_list, container, false);
		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));

		adapter = new RecentlyWatchedRecyclerViewAdapter(items, listener, context);
		recyclerView.setAdapter(adapter);
		refreshItem(context);

		return recyclerView;
	}

	public void refreshItem(Context context) {
		recentlyWatchedUC.fetchRecentlyWatched(new Callback<List<VideoItem>>() {
			@Override
			public void onSuccess(List<VideoItem> response) {
				items.clear();
				items.addAll(response);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				Log.d(TAG, "onfailure");
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		DaggerSampleComponent.builder().sampleModule(new SampleModule(context)).build().inject(this);
		if (context instanceof OnRecentlyWatchedFragmentInteractionListener) {
			listener = (OnRecentlyWatchedFragmentInteractionListener) context;
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
	public interface OnRecentlyWatchedFragmentInteractionListener {
		void onRecentlyWatchedFragmentInteraction(VideoItem item);
	}
}