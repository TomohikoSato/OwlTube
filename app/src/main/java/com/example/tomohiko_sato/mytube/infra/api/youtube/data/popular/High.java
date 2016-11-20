
package com.example.tomohiko_sato.mytube.infra.api.youtube.data.popular;




public class High {

    public String url;
    public Integer width;
    public Integer height;

    /**
     * No args constructor for use in serialization
     * 
     */
    public High() {
    }

    /**
     * 
     * @param height
     * @param width
     * @param url
     */
    public High(String url, Integer width, Integer height) {
        this.url = url;
        this.width = width;
        this.height = height;
    }

}
