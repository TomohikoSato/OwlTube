package com.example.tomohiko_sato.owltube.domain.util;

/**
 * 非同期処理の結果を渡すために使うコールバッククラス
 * <p>
 * ドメインより下層ではネットワーク処理、DBアクセス等重い処理を行うことが多いので、ドメインでは非同期処理をする。非同期処理の結果を渡すのにこのクラスを使う。
 * </p>
 */
public class Callback<T> {
	public void onSuccess(T response) {
	}

	public void onFailure(Throwable t) {
	}
}
