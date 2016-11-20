package com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular;

public class Item {

	public String kind;
	public String etag;
	public String id;
	public Snippet snippet;
	public Statistics statistics;

	public Item() {
	}

	public Item(String kind, String etag, String id, Snippet snippet, Statistics statistics) {
		this.kind = kind;
		this.etag = etag;
		this.id = id;
		this.snippet = snippet;
		this.statistics = statistics;
	}
}
