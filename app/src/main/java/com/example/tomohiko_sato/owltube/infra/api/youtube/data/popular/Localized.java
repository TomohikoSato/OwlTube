
package com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular;




public class Localized {

    public String title;
    public String description;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Localized() {
    }

    /**
     * 
     * @param title
     * @param description
     */
    public Localized(String title, String description) {
        this.title = title;
        this.description = description;
    }

}
