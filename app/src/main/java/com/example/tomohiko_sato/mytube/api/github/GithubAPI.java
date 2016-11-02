package com.example.tomohiko_sato.mytube.api.github;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface GithubAPI {

/*
	@GET("user/{user}/repos")
	Call<List<User>> listRepos(@Path("user") String user);
*/

	@GET("users/{user}")
	Call<User> getUser(@Path("user") String user);
}
