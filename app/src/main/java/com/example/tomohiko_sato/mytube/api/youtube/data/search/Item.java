package com.example.tomohiko_sato.mytube.api.youtube.data.search;

public class Item {

	public String kind;
	public String etag;
	public Id id;
	public Snippet snippet;

	/**
	 * @param id
	 * @param etag
	 * @param snippet
	 * @param kind
	 */
	public Item(String kind, String etag, Id id, Snippet snippet) {
		this.kind = kind;
		this.etag = etag;
		this.id = id;
		this.snippet = snippet;
	}
}
