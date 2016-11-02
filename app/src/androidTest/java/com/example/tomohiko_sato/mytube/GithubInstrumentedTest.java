package com.example.tomohiko_sato.mytube;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.tomohiko_sato.mytube.api.github.GithubRequest;
import com.example.tomohiko_sato.mytube.api.github.User;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class GithubInstrumentedTest {
	private GithubRequest request = null;
	private final String TAG = GithubInstrumentedTest.class.getSimpleName();

	@Before
	public void before() {
		this.request = new GithubRequest();
	}

	@Test
	public void syncRequest() {
		Log.d(TAG, "hoge");
		Response<User> repo = request.getUserSync("octcat");
		assertEquals("octcat", repo.body().login);
	}

	@Test
	public void asyncRequest() {
		Callback<User> callback= new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				System.out.print(String.format("Response: %s", response.body()));
				Log.d(TAG, "response");
				Assert.assertEquals("octcat", response.body().login);
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				Log.d(TAG, "failure");
				System.out.print(t.toString());
				fail();
			}
		};

		request.getUserAsync("octcat", callback);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void useAppContext() throws Exception {
		// Context of the app under test.
		Context appContext = InstrumentationRegistry.getTargetContext();

		assertEquals("com.example.tomohiko_sato.mytube", appContext.getPackageName());
	}
}
