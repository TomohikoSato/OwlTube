package com.example.tomohiko_sato.mytube.presentation.search;

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

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.di.DaggerSampleComponent;
import com.example.tomohiko_sato.mytube.di.SampleModule;
import com.example.tomohiko_sato.mytube.domain.data.VideoItem;
import com.example.tomohiko_sato.mytube.domain.search.SearchUseCase;
import com.example.tomohiko_sato.mytube.domain.util.Callback;

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
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_searchhistory_list, container, false);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		new ArrayList<String>();

		adapter = new SearchHistoryRecyclerViewAdapter(new ArrayList<String>() {{
			add("A");
			add("B");
			add("C");
		}}, listener);

		recyclerView.setAdapter(adapter);

		return recyclerView;
	}


	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		DaggerSampleComponent.builder().sampleModule(new SampleModule(context)).build().inject(this);

		searchUC.fetchSearchHistories(new Callback<List<String>>() {
			@Override
			public void onSuccess(List<String> searchHistories) {

			}

			@Override
			public void onFailure(Throwable t) {
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
