package com.luj8n.repofinder;

public class RepoResult {
    private String name;
    private String url;
    private String description;
    private boolean highlighted;

    public RepoResult(String name, String url, String description, boolean highlighted) {
        this.name = name;
        this.url = url;
        this.description = description;
        this.highlighted = highlighted;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isHighlighted() {
        return highlighted;
    }

    public void setHighlighted(boolean highlighted) {
        this.highlighted = highlighted;
    }
}
