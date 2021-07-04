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
        return "Name: " + name + "\n"
                + "Details: " + details + "\n"
                + "Url: " + url;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Organization)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Organization that = (Organization) o;

        if (isSharedTickets() != that.isSharedTickets()) {
            return false;
        }
        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) {
            return false;
        }
        if (getDomainNames() != null ? !getDomainNames().equals(that.getDomainNames()) : that.getDomainNames() != null) {
            return false;
        }
        return getDetails() != null ? getDetails().equals(that.getDetails()) : that.getDetails() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getDomainNames() != null ? getDomainNames().hashCode() : 0);
        result = 31 * result + (getDetails() != null ? getDetails().hashCode() : 0);
        result = 31 * result + (isSharedTickets() ? 1 : 0);
        return result;
    }
}
