package com.example.tomohiko_sato.owltube.infra.api.youtube.data.search;

public class Id {

	public String kind;
	public String videoId;

	/**
	 * No args constructor for use in serialization
	 */
	public Id() {
	}

	/**
	 * @param videoId
	 * @param kind
	 */
	public Id(String kind, String videoId) {
		this.kind = kind;
		this.videoId = videoId;
	}

}
