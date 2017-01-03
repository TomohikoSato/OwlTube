package com.example.tomohiko_sato.owltube.infra.api.google;

import com.example.tomohiko_sato.owltube.infra.api.mapper.SuggestMapper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleRequest {
	private final static String TAG = GoogleRequest.class.getSimpleName();

	private final OkHttpClient client = new OkHttpClient();

	public GoogleRequest() {
	}

	public Observable<List<String>> fetchSuggestKeywordForYoutube(String keyword) {
		final Request request = new Request.Builder()
				.url("http://suggestqueries.google.com/complete/search?client=firefox&ds=yt&q=" + keyword)
				.get()
				.build();

		return Observable.create(new ObservableOnSubscribe<String>() {
			@Override
			public void subscribe(ObservableEmitter<String> e) throws Exception {
				Response response = client.newCall(request).execute();
				e.onNext(response.body().string());
				e.onComplete();
			}
		}).map(SuggestMapper::map);
	}
}