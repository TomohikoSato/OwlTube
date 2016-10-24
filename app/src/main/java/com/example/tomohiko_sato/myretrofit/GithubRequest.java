package com.example.tomohiko_sato.myretrofit;

import java.lang.annotation.RetentionPolicy;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by tomohiko_sato on 10/24/16.
 */

public interface GithubRequest {
/*
	@GET("user/{user}/repos")
	Call<List<Repo>> listRepos(@Path("user") String user);
*/
	@GET("users/{user}")
	Call<Repo> getUser(@Path("user") String user);
}
