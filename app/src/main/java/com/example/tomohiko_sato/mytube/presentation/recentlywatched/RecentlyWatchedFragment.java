package com.example.tomohiko_sato.mytube.presentation.recentlywatched;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Item;
import com.example.tomohiko_sato.mytube.api.youtube.data.popular.Popular;
import com.example.tomohiko_sato.mytube.config.AppConst;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnRecentlyWatchedFragmentInteractionListener}
 * interface.
 */
public class RecentlyWatchedFragment extends Fragment {
	private static final String TAG = RecentlyWatchedFragment.class.getSimpleName();
	private OnRecentlyWatchedFragmentInteractionListener listener;

	public RecentlyWatchedFragment() {
	}

	public static RecentlyWatchedFragment newInstance() {
		return new RecentlyWatchedFragment();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	List<Item> items = new ArrayList<>();
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
		SharedPreferences recentlyWatchedSP = context.getSharedPreferences(AppConst.Pref.NAME, 0);
		Set<String> set = recentlyWatchedSP.getStringSet(AppConst.Pref.KEY_RECENTLY_WATCHED, new HashSet<String>());

		set.add("Im_u7DwWo0w");
		set.add("a9n_4d64dUw");

		final ArrayList<String> videoIdList = new ArrayList<>();
		videoIdList.addAll(set);
		new YoutubeRequest().fetch(videoIdList, new Callback<Popular>() {
			@Override
			public void onResponse(Call<Popular> call, Response<Popular> response) {
				Log.d(TAG, "onresponse");
				items.addAll(response.body().items);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Call<Popular> call, Throwable t) {
				Log.d(TAG, "onfailure");
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
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
		void onRecentlyWatchedFragmentInteraction(String videoId);
	}
}