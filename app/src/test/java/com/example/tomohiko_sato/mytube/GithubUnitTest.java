package com.example.tomohiko_sato.mytube;

import com.example.tomohiko_sato.mytube.api.github.GithubRequest;
import com.example.tomohiko_sato.mytube.api.github.User;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class GithubUnitTest {
	private static final String TAG = GithubUnitTest.class.getSimpleName();
	private GithubRequest request = null;

	@BeforeClass
	public static void beforeClass() {

	}

	@Before
	public void before() {
		this.request = new GithubRequest();
	}

	@Test
	public void asyncRequest() {
		request.getUserAsync("octcat", new Callback<User>() {
			@Override
			public void onResponse(Call<User> call, Response<User> response) {
				System.out.print(String.format("Response: %s", response.body()));
			}

			@Override
			public void onFailure(Call<User> call, Throwable t) {
				System.out.print(t.toString());
			}
		});
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void syncRequest() {

		Response<User> repo = request.getUserSync("octcat");


		// repo.body().name == "octcat"
		System.out.print(repo.raw().toString());
		assertEquals("octcat", repo.body().login);

	}
}