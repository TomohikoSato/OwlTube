package com.example.tomohiko_sato.mytube.api.youtube.data;

import java.util.ArrayList;
import java.util.List;

public class Search {

	public String kind;
	public String etag;
	public String nextPageToken;
	public String regionCode;
	public PageInfo pageInfo;
	public List<Item> items = new ArrayList<Item>();

	/**
	 * No args constructor for use in serialization
	 */
	public Search() {
	}

	/**
	 * @param regionCode
	 * @param etag
	 * @param items
	 * @param pageInfo
	 * @param nextPageToken
	 * @param kind
	 */
	public Search(String kind, String etag, String nextPageToken, String regionCode, PageInfo pageInfo, List<Item> items) {
		this.kind = kind;
		this.etag = etag;
		this.nextPageToken = nextPageToken;
		this.regionCode = regionCode;
		this.pageInfo = pageInfo;
		this.items = items;
	}
}
