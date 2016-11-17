package com.example.tomohiko_sato.mytube.api.youtube.data.popular;

import java.util.ArrayList;
import java.util.List;

public class Popular {

	public String kind;
	public String etag;
	public String nextPageToken;
	public PageInfo pageInfo;
	public List<Item> items = new ArrayList<Item>();

	/**
	 * No args constructor for use in serialization
	 */
	public Popular() {
	}

	/**
	 * @param etag
	 * @param items
	 * @param pageInfo
	 * @param nextPageToken
	 * @param kind
	 */
	public Popular(String kind, String etag, String nextPageToken, PageInfo pageInfo, List<Item> items) {
		this.kind = kind;
		this.etag = etag;
		this.nextPageToken = nextPageToken;
		this.pageInfo = pageInfo;
		this.items = items;
	}

}
