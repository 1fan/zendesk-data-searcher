package com.zendesk.datasearcher.model.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Organization extends AbstractEntity {

    private String name;
    @SerializedName("domain_names")
    private List<String> domainNames;
    private String details;
    @SerializedName("shared_tickets")
    private boolean sharedTickets;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getDomainNames() {
        return domainNames;
    }

    public void setDomainNames(List<String> domainNames) {
        this.domainNames = domainNames;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public boolean isSharedTickets() {
        return sharedTickets;
    }

    public void setSharedTickets(boolean sharedTickets) {
        this.sharedTickets = sharedTickets;
    }

    @Override
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Name: ").append(name).append("\n")
                .append("Details: ").append(details).append("\n")
                .append("Url: ").append(url);
        return summary.toString();
    }

    @Override
    public String toString() {
        return "id: '" + id + "\n" +
                "created_at: '" + createdAt + "\n" +
                "tags: " + tags + "\n" +
                "external_id: '" + externalId + "\n" +
                "url: '" + url + "\n" +
                "name: '" + name + "\n" +
                "domain_names: " + domainNames + "\n" +
                "details: '" + details + "\n" +
                "shared_tickets: " + sharedTickets;
    }
}
