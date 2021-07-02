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

    public abstract String getSummary();
}
