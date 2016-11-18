package com.example.tomohiko_sato.mytube.presentation.recentlywatched;

import android.content.Context;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

/**
 * A simple {@link ListFragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecentlyWatchedFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentlyWatchedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentlyWatchedFragment extends ListFragment {
	public static final String[] Data = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};

	private OnRecentlyWatchedFragmentInteractionListener listener;

	public RecentlyWatchedFragment() {
		// Required empty public constructor
	}

	public static RecentlyWatchedFragment newInstance() {
		RecentlyWatchedFragment fragment = new RecentlyWatchedFragment();
		return fragment;
	}

/*
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recently_watched, container, false);
	}
*/

	@Override
	public void onAttach(Context context) {
		if (!(context instanceof OnRecentlyWatchedFragmentInteractionListener)) {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnTopFragmentInteractionListener to attached activity");
		}

		super.onAttach(context);
		// DBへデータ取りに行く

		ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, Data);
		setListAdapter(adapter);
		listener = (OnRecentlyWatchedFragmentInteractionListener) context;
		getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				listener.onRecentlyWatchedFragmentInteraction("some videoid");
			}
		});
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