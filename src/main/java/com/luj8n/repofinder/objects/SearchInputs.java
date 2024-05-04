package com.luj8n.repofinder.objects;

public class SearchInputs {
    /**
     * The name or link of a GitHub organization.
     */
    private String organization;
    /**
     * The GitHub access token.
     */
    private String token;
    /**
     * The url of the api. It overrides 'organization'. Used for paging.
     */
    private String link;

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
