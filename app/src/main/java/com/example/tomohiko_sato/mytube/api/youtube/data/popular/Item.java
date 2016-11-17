package com.example.tomohiko_sato.mytube.api.youtube.data.popular;

public class Item {

	public String kind;
	public String etag;
	public String id;
	public Snippet snippet;

	/**
	 * No args constructor for use in serialization
	 */
	public Item() {
	}

	/**
	 * @param id
	 * @param etag
	 * @param snippet
	 * @param kind
	 */
	public Item(String kind, String etag, String id, Snippet snippet) {
		this.kind = kind;
		this.etag = etag;
		this.id = id;
		this.snippet = snippet;
	}

}
