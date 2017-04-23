package com.example.tomohiko_sato.owltube.presentation.top;


import android.content.Context;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Nikola D. on 3/15/2016.
 */
public final class BottomNavigationBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {

	public BottomNavigationBehavior() {
		super();
	}

	public BottomNavigationBehavior(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onStartNestedScroll(
			CoordinatorLayout coordinatorLayout, BottomNavigationView child,
			View directTargetChild, View target, int nestedScrollAxes) {
		return nestedScrollAxes == ViewCompat.SCROLL_AXIS_VERTICAL;
	}

	@Override
	public void onNestedScroll(CoordinatorLayout coordinatorLayout,
							   BottomNavigationView child, View target, int dxConsumed, int dyConsumed,
							   int dxUnconsumed, int dyUnconsumed) {
		if (dyConsumed > 0) {
//			Logger.e("hide");
		} else {
//			Logger.e("show");
		}
	}
}