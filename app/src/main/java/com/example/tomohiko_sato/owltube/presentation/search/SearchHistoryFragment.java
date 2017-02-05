package com.example.tomohiko_sato.owltube.presentation.search;

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
import com.example.tomohiko_sato.owltube.domain.search.SearchUseCase;

import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnSearchHistoryFragmentInteractionListener}
 * interface.
 */
public class SearchHistoryFragment extends Fragment {
	private OnSearchHistoryFragmentInteractionListener listener;
	private SearchHistoryViewAdapter adapter;

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
		adapter = new SearchHistoryViewAdapter(new ArrayList<String>(), listener);

		recyclerView.setAdapter(adapter);

		return recyclerView;
	}


	@Override
	public void onAttach(final Context context) {
		super.onAttach(context);
		((OwlTubeApp) context.getApplicationContext()).getComponent().inject(this);

		searchUC.fetchSearchHistories()
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(histories -> adapter.refreshSearchHistories(histories), Throwable::printStackTrace);

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
