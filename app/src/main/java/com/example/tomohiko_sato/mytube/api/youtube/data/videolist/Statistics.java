package com.example.tomohiko_sato.mytube.api.youtube.data.videolist;

public class Statistics {

    public String viewCount;
    public String likeCount;
    public String dislikeCount;
    public String favoriteCount;
    public String commentCount;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Statistics() {
    }

    /**
     * 
     * @param favoriteCount
     * @param dislikeCount
     * @param likeCount
     * @param commentCount
     * @param viewCount
     */
    public Statistics(String viewCount, String likeCount, String dislikeCount, String favoriteCount, String commentCount) {
        this.viewCount = viewCount;
        this.likeCount = likeCount;
        this.dislikeCount = dislikeCount;
        this.favoriteCount = favoriteCount;
        this.commentCount = commentCount;
    }
}
