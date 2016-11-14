package com.example.tomohiko_sato.mytube;

import android.support.test.runner.AndroidJUnit4;
import android.util.Log;

import com.example.tomohiko_sato.mytube.api.youtube.YoutubeRequest;
import com.example.tomohiko_sato.mytube.api.youtube.data.search.Search;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class YoutubeRequestInstrumentedTest {
	private YoutubeRequest request = null;
	private final String TAG = YoutubeRequestInstrumentedTest.class.getSimpleName();

	@Before
	public void before() {
		this.request = new YoutubeRequest();
	}

	@Test
	public void syncRequest() {
		Response<Search> repo = request.searchSync("dir en grey");
		assertEquals("JP", repo.body().regionCode);
		assertEquals("youtube#searchResult", repo.body().items.iterator().next().kind);
	}

	@Test
	public void asyncRequest() {
		Callback<Search> callback= new Callback<Search>() {
			@Override
			public void onResponse(Call<Search> call, Response<Search> response) {
				System.out.print(String.format("Response: %s", response.body()));
				Log.d(TAG, "response");
				Assert.assertEquals("JP", response.body().regionCode);
			}

			@Override
			public void onFailure(Call<Search> call, Throwable t) {
				Log.d(TAG, "failure");
				System.out.print(t.toString());
				fail();
			}
		};

		request.searchAsync("dir en grey", callback);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
