package com.example.tomohiko_sato.mytube.presentation.main.popular;

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
import com.example.tomohiko_sato.mytube.domain.popular.PopularUseCase;
import com.example.tomohiko_sato.mytube.infra.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Item;
import com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular.Popular;

import java.util.ArrayList;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PopularFragment extends Fragment {
	private final static String TAG = PopularFragment.class.getSimpleName();
	private OnTopFragmentInteractionListener mListener;
	PopularItemAdapter adapter;

	@Inject
	PopularUseCase popularUC;

	public static PopularFragment newInstance() {
		PopularFragment fragment = new PopularFragment();
		return fragment;
	}

	public PopularFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DaggerSampleComponent.create().inject(this);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
							 Bundle savedInstanceState) {
		RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.fragment_item_list, container, false);

		Context context = recyclerView.getContext();
		recyclerView.setLayoutManager(new LinearLayoutManager(context));
		adapter = new PopularItemAdapter(new ArrayList<Item>(), mListener, context);
		recyclerView.setAdapter(adapter);

		popularUC.fetchPopular(new Callback<Popular>() {
			@Override
			public void onResponse(Call<Popular> call, Response<Popular> response) {
				Log.d(TAG, "size " + response.body().items.size());
				adapter.setItems(response.body().items);
				adapter.notifyDataSetChanged();
			}

			@Override
			public void onFailure(Call<Popular> call, Throwable t) {
			}
		});

		return recyclerView;
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
		if (context instanceof OnTopFragmentInteractionListener) {
			mListener = (OnTopFragmentInteractionListener) context;
		} else {
			throw new UnsupportedOperationException(context.toString()
					+ " must implement OnTopFragmentInteractionListener to attached activity");
		}
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mListener = null;
	}

	/**
	 * This interface must be implemented by activities that contain this
	 * fragment to allow an interaction in this fragment to be communicated
	 * to the activity and potentially other fragments contained in that
	 * activity.
	 */
	public interface OnTopFragmentInteractionListener {
		void onTopFragmentInteraction(Item item);
	}
}
