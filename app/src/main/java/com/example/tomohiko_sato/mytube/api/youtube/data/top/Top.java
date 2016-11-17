package com.example.tomohiko_sato.mytube.api.youtube.data.top;

import java.util.ArrayList;
import java.util.List;

public class Top {

	public String kind;
	public String etag;
	public String nextPageToken;
	public PageInfo pageInfo;
	public List<Item> items = new ArrayList<Item>();

	/**
	 * No args constructor for use in serialization
	 */
	public Top() {
	}

	/**
	 * @param etag
	 * @param items
	 * @param pageInfo
	 * @param nextPageToken
	 * @param kind
	 */
	public Top(String kind, String etag, String nextPageToken, PageInfo pageInfo, List<Item> items) {
		this.kind = kind;
		this.etag = etag;
		this.nextPageToken = nextPageToken;
		this.pageInfo = pageInfo;
		this.items = items;
	}

}
