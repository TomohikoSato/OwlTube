
package com.example.tomohiko_sato.owltube.infra.api.youtube.data.popular;




public class PageInfo {

    public Integer totalResults;
    public Integer resultsPerPage;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PageInfo() {
    }

    /**
     * 
     * @param totalResults
     * @param resultsPerPage
     */
    public PageInfo(Integer totalResults, Integer resultsPerPage) {
        this.totalResults = totalResults;
        this.resultsPerPage = resultsPerPage;
    }

}
