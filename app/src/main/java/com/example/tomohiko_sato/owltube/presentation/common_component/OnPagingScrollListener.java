package com.example.tomohiko_sato.owltube.presentation.common_component;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.OnScrollListener;

import com.example.tomohiko_sato.owltube.util.Logger;


/**
 * {@link RecyclerView}のスクロールを検知して、ページングのイベントを発行するクラス
 * {@link LinearLayoutManager} を使っていることを前提としている
 */
public class OnPagingScrollListener extends OnScrollListener {

	/**
	 * ページングする必要があることを伝える
	 */
	public interface OnShouldLoadNextPageListener {
		/**
		 * @param lastItemPosition 最後のリスト要素の位置
		 */
		void onShouldLoadNextPage(int lastItemPosition);
	}

	private boolean isLoading = false;
	private final static int BUFFER_TO_THE_LAST_DEFAULT = 10;

	/**
	 * Loadが完了したら呼ぶ。{@link OnShouldLoadNextPageListener#onShouldLoadNextPage}がまた発行されるようになる。
	 */
	public void onLoadCompleted() {
		isLoading = false;
	}

	/**
	 * 最後からいくつ前のアイテムが表示された時にイベントを発行するか
	 */
	private final int bufferToTheLast;
	private final OnShouldLoadNextPageListener listener;

	public OnPagingScrollListener(int bufferToTheLast, OnShouldLoadNextPageListener listener) {
		this.bufferToTheLast = bufferToTheLast;
		this.listener = listener;
	}

	public OnPagingScrollListener(OnShouldLoadNextPageListener listener) {
		this.bufferToTheLast = BUFFER_TO_THE_LAST_DEFAULT;
		this.listener = listener;
	}

	@Override
	public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
		if (dy > 0) { //check for scroll down
			if (!isLoading) { //データを取ってきている時は処理をしない
				LinearLayoutManager llm = (LinearLayoutManager) recyclerView.getLayoutManager();

				int visibleItemCount = llm.getChildCount();
				int totalItemCount = llm.getItemCount();
				int pastVisiblesItems = llm.findFirstVisibleItemPosition();

				if ((visibleItemCount + pastVisiblesItems + bufferToTheLast) >= totalItemCount) {
					Logger.d("Last Item Wow !");
					listener.onShouldLoadNextPage(totalItemCount);
					isLoading = true;
				}
			}
		}
	}
}
