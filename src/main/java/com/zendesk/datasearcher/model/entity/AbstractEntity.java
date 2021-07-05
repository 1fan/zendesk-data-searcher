package com.zendesk.datasearcher.model.entity;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public abstract class AbstractEntity {
    @SerializedName("_id")
    protected String id;

    @SerializedName("created_at")
    protected String createdAt;
    protected List<String> tags;
    @SerializedName("external_id")
    protected String externalId;

    protected String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * Get the summary of the entity by including the key fields only.
     *
     * @return Get the summary of the entity.
     */
    public abstract String getSummary();

    /**
     * Get all fields' value of the entity into String to be displayed.
     *
     * @return the entity value in String including all fields.
     */
    public abstract String toString();

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractEntity)) {
            return false;
        }

        AbstractEntity that = (AbstractEntity) o;

        if (getId() != null ? !getId().equals(that.getId()) : that.getId() != null) {
            return false;
        }
        if (getCreatedAt() != null ? !getCreatedAt().equals(that.getCreatedAt()) : that.getCreatedAt() != null) {
            return false;
        }
        if (getTags() != null ? !getTags().equals(that.getTags()) : that.getTags() != null) {
            return false;
        }
        if (getExternalId() != null ? !getExternalId().equals(that.getExternalId()) : that.getExternalId() != null) {
            return false;
        }
        return getUrl() != null ? getUrl().equals(that.getUrl()) : that.getUrl() == null;
    }

    @Override
    public int hashCode() {
        int result = getId() != null ? getId().hashCode() : 0;
        result = 31 * result + (getCreatedAt() != null ? getCreatedAt().hashCode() : 0);
        result = 31 * result + (getTags() != null ? getTags().hashCode() : 0);
        result = 31 * result + (getExternalId() != null ? getExternalId().hashCode() : 0);
        result = 31 * result + (getUrl() != null ? getUrl().hashCode() : 0);
        return result;
    }
}
