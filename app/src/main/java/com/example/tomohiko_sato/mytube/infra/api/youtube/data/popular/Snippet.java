
package com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular;




public class Snippet {

    public String publishedAt;
    public String channelId;
    public String title;
    public String description;
    public Thumbnails thumbnails;
    public String channelTitle;
    public String categoryId;
    public String liveBroadcastContent;
    public Localized localized;

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
     * @param categoryId
     * @param channelTitle
     * @param thumbnails
     * @param liveBroadcastContent
     * @param localized
     */
    public Snippet(String publishedAt, String channelId, String title, String description, Thumbnails thumbnails, String channelTitle, String categoryId, String liveBroadcastContent, Localized localized) {
        this.publishedAt = publishedAt;
        this.channelId = channelId;
        this.title = title;
        this.description = description;
        this.thumbnails = thumbnails;
        this.channelTitle = channelTitle;
        this.categoryId = categoryId;
        this.liveBroadcastContent = liveBroadcastContent;
        this.localized = localized;
    }

}
