package com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular;

public class Default {

	public String url;
	public Integer width;
	public Integer height;

	/**
	 * No args constructor for use in serialization
	 */
	public Default() {
	}

	/**
	 * @param height
	 * @param width
	 * @param url
	 */
	public Default(String url, Integer width, Integer height) {
		this.url = url;
		this.width = width;
		this.height = height;
	}

}
