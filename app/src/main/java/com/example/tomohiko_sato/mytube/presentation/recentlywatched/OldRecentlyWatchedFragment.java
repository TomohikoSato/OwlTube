package com.example.tomohiko_sato.mytube.presentation.recentlywatched;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.tomohiko_sato.mytube.R;
import com.example.tomohiko_sato.mytube.config.AppConst;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecentlyWatchedFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OldRecentlyWatchedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OldRecentlyWatchedFragment extends ListFragment {
	public static final String[] Data = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private OnRecentlyWatchedFragmentInteractionListener listener;

	public OldRecentlyWatchedFragment() {
		// Required empty public constructor
	}

	public static OldRecentlyWatchedFragment newInstance() {
		OldRecentlyWatchedFragment fragment = new OldRecentlyWatchedFragment();
		return fragment;
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recently_watched, container, false);
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listener.onRecentlyWatchedFragmentInteraction("some videoid");
			}
		});
	}

	@Override
	public void onAttach(Context context) {
		if (!(context instanceof OnRecentlyWatchedFragmentInteractionListener)) {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnTopFragmentInteractionListener to attached activity");
		}

		super.onAttach(context);
		// DBへデータ取りに行く

		SharedPreferences recentlyWatchedSP = context.getSharedPreferences(AppConst.Pref.NAME_RECENTLY_WATCHED, 0);
		HashSet<String> set = new HashSet<String>();
		set.add("Im_u7DwWo0w");
		set.add("hogehoge");

		Set<String> videoIds = recentlyWatchedSP.getStringSet(AppConst.Pref.KEY_RECENTLY_WATCHED, set);

		ArrayList<String> list = new ArrayList<>();
		list.addAll(videoIds);
		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.search_result_list_item, R.id.channel_title, list);

		setListAdapter(adapter);
		listener = (OnRecentlyWatchedFragmentInteractionListener) context;
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
	 */
	public interface OnRecentlyWatchedFragmentInteractionListener {
		void onRecentlyWatchedFragmentInteraction(String videoId);
	}
}