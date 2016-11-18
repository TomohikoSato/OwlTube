package com.example.tomohiko_sato.mytube.presentation.recentlywatched;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tomohiko_sato.mytube.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnRecentlyWatchedFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RecentlyWatchedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecentlyWatchedFragment extends Fragment {
	private OnRecentlyWatchedFragmentInteractionListener listener;

	public RecentlyWatchedFragment() {
		// Required empty public constructor
	}

	public static RecentlyWatchedFragment newInstance() {
		RecentlyWatchedFragment fragment = new RecentlyWatchedFragment();
		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_recently_watched, container, false);
	}

	public void onButtonPressed(Uri uri) {
		if (listener != null) {
			listener.onRecentlyWatchedFragmentInteraction(uri);
		}
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnRecentlyWatchedFragmentInteractionListener) {
			listener = (OnRecentlyWatchedFragmentInteractionListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnTopFragmentInteractionListener to attached activity");
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
	 */
	public interface OnRecentlyWatchedFragmentInteractionListener {
		void onRecentlyWatchedFragmentInteraction(Uri uri);
	}
}
