package com.example.tomohiko_sato.owltube.presentation.top;


import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by Nikola D. on 3/15/2016.
 */
public final class BottomNavigationBehavior<V extends View> extends VerticalScrollingBehavior<V> {
	private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();
	private boolean hidden = false;
	private ViewPropertyAnimatorCompat mOffsetValueAnimator;
	private boolean scrollingEnabled = true;
	int[] attrsArray = new int[]{
			android.R.attr.id, android.R.attr.elevation};
	private int mElevation = 8;

	public BottomNavigationBehavior() {
		super();
	}

	public BottomNavigationBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				attrsArray);
		mElevation = a.getResourceId(1, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, mElevation, context.getResources().getDisplayMetrics()));
		a.recycle();
	}

	@Override
	public void onNestedVerticalOverScroll(CoordinatorLayout coordinatorLayout, V child, @ScrollDirection int direction, int currentOverScroll, int totalOverScroll) {
	}

	@Override
	public void onDirectionNestedPreScroll(CoordinatorLayout coordinatorLayout, V child, View target, int dx, int dy, int[] consumed, @ScrollDirection int scrollDirection) {
		handleDirection(child, scrollDirection);
	}

	private void handleDirection(V child, @ScrollDirection int scrollDirection) {
		if (!scrollingEnabled) return;
		if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
			hidden = false;
			animateOffset(child, 0);
		} else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
			hidden = true;
			animateOffset(child, child.getHeight());
		}
	}

	@Override
	protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, V child, View target, float velocityX, float velocityY, @ScrollDirection int scrollDirection) {
		handleDirection(child, scrollDirection);
		return true;
	}

	private void animateOffset(final V child, final int offset) {
		ensureOrCancelAnimator(child);
		mOffsetValueAnimator.translationY(offset).start();
	}


	private void ensureOrCancelAnimator(@NonNull V child) {
		if (mOffsetValueAnimator == null) {
			mOffsetValueAnimator = ViewCompat.animate(child);
			mOffsetValueAnimator.setDuration(100);
			mOffsetValueAnimator.setInterpolator(INTERPOLATOR);
		} else {
			mOffsetValueAnimator.cancel();
		}
	}

}