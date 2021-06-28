package com.zendesk.datasearcher.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class Organizations extends AbstractEntity {

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
    public String toString() {
        return "Organizations{" +
                "_id='" + id + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", tags=" + tags +
                ", externalId='" + externalId + '\'' +
                ", url='" + url + '\'' +
                ", name='" + name + '\'' +
                ", domainNames=" + domainNames +
                ", details='" + details + '\'' +
                ", sharedTickets=" + sharedTickets +
                '}';
    }
}
