package com.luj8n.repofinder.objects;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<RepoResult> repos;
    private String nextUrl;
    private String prevUrl;

    public SearchResult(List<RepoResult> repos, String nextUrl, String prevUrl) {
        this.repos = repos;
        this.nextUrl = nextUrl;
        this.prevUrl = prevUrl;
    }

    public SearchResult() {
        repos = new ArrayList<>();
    }

    public List<RepoResult> getRepos() {
        return repos;
    }

    public void setRepos(List<RepoResult> repos) {
        this.repos = repos;
    }

    public String getNextUrl() {
        return nextUrl;
    }

    public void setNextUrl(String nextUrl) {
        this.nextUrl = nextUrl;
    }

    public String getPrevUrl() {
        return prevUrl;
    }

    public void setPrevUrl(String prevUrl) {
        this.prevUrl = prevUrl;
    }
}
