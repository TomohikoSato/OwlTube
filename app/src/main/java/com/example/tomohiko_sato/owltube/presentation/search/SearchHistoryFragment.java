package com.example.tomohiko_sato.owltube.presentation.search;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.tomohiko_sato.owltube.R;
import com.example.tomohiko_sato.owltube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.owltube.di.SampleModule;
import com.example.tomohiko_sato.owltube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.owltube.domain.util.Callback;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSearchHistoryFragmentInteractionListener}
 * interface.
 */
public class SearchHistoryFragment extends Fragment {
	private final static String TAG = SearchHistoryFragment.class.getSimpleName();

	private OnSearchHistoryFragmentInteractionListener listener;
	private SearchHistoryRecyclerViewAdapter adapter;

	@Inject
	SearchUseCase searchUC;

	public static SearchHistoryFragment newInstance() {
		SearchHistoryFragment fragment = new SearchHistoryFragment();
		return fragment;
	}

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public SearchHistoryFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_search_history, container, false);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		new ArrayList<String>();

		adapter = new SearchHistoryRecyclerViewAdapter(new ArrayList<String>(), listener);

		recyclerView.setAdapter(adapter);

		return recyclerView;
	}


	@Override
	public void onAttach(final Context context) {
		super.onAttach(context);
		DaggerSampleComponent.builder().sampleModule(new SampleModule(context)).build().inject(this);

		searchUC.fetchSearchHistories(new Callback<List<String>>() {
			@Override
			public void onSuccess(List<String> searchHistories) {
				adapter.refreshSearchHistories(searchHistories);
			}

			@Override
			public void onFailure(Throwable t) {
				t.printStackTrace();
				Log.e(TAG, "search histories onFailure");
			}
		});
		if (context instanceof OnSearchHistoryFragmentInteractionListener) {
			listener = (OnSearchHistoryFragmentInteractionListener) context;
		} else {
			throw new RuntimeException(context.toString()
					+ " must implement OnSearchHistoryFragmentInteractionListener");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		listener = null;
	}

	public interface OnSearchHistoryFragmentInteractionListener {
		void OnSearchHistoryFragmentInteraction(String searchHistory);
	}
}
