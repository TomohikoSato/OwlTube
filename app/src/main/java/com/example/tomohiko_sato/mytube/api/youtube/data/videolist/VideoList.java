package com.example.tomohiko_sato.mytube.api.youtube.data.videolist;

import java.util.ArrayList;
import java.util.List;

public class VideoList {

    public String kind;
    public String etag;
    public PageInfo pageInfo;
    public List<Item> items = new ArrayList<Item>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public VideoList() {
    }

    /**
     * 
     * @param etag
     * @param items
     * @param pageInfo
     * @param kind
     */
    public VideoList(String kind, String etag, PageInfo pageInfo, List<Item> items) {
        this.kind = kind;
        this.etag = etag;
        this.pageInfo = pageInfo;
        this.items = items;
    }
}
