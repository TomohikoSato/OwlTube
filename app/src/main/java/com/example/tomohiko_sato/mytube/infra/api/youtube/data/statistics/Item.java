package com.example.tomohiko_sato.mytube.infra.api.youtube.data.statistics;

public class Item {

	public String kind;
	public String etag;
	public String id;
	public Statistics statistics;

	/**
	 * No args constructor for use in serialization
	 */
	public Item() {
	}

	/**
	 * @param id
	 * @param etag
	 * @param kind
	 * @param statistics
	 */
	public Item(String kind, String etag, String id, Statistics statistics) {
		this.kind = kind;
		this.etag = etag;
		this.id = id;
		this.statistics = statistics;
	}

}
