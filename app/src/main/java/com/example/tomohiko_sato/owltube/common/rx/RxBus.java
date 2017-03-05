package com.example.tomohiko_sato.owltube.common.rx;


import com.example.tomohiko_sato.owltube.common.util.Logger;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.subjects.PublishSubject;

/**
 * EventBus的な仕組みを実現するクラス
 */
public class RxBus {
	private final PublishSubject<Event> bus = PublishSubject.create();

	public void send(Event e) {
		Logger.d("send on RxBus:" + e);
		bus.onNext(e);
	}

	public <T extends Event> Disposable register(Class<T> clazz, Consumer<T> handler) {
		Logger.d("registered RxBus:" + clazz.getCanonicalName());
		return bus.ofType(clazz).subscribe(handler);
	}

	public <T> Disposable registerOnMainThread(Class<T> clazz, Consumer<T> handler) {
		Logger.d("registered RxBus:" + clazz.getCanonicalName());
		return bus.ofType(clazz).observeOn(AndroidSchedulers.mainThread()).subscribe(handler);
	}

	public boolean hasObservers() {
		return bus.hasObservers();
	}

	/**
	 * marker interface
	 */
	public interface Event {
	}
}
