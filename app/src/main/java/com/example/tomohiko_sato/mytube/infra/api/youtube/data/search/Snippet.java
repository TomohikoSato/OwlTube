
package com.example.tomohiko_sato.mytube.infra.api.youtube.data.search;




public class Snippet {

    public String publishedAt;
    public String channelId;
    public String title;
    public String description;
    public Thumbnails thumbnails;
    public String channelTitle;
    public String liveBroadcastContent;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Snippet() {
    }

    /**
     * 
     * @param publishedAt
     * @param title
     * @param channelId
     * @param description
     * @param channelTitle
     * @param thumbnails
     * @param liveBroadcastContent
     */
    public Snippet(String publishedAt, String channelId, String title, String description, Thumbnails thumbnails, String channelTitle, String liveBroadcastContent) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
        this.liveBroadcastContent = liveBroadcastContent;
    }

}
