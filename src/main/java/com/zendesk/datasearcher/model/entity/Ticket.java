package com.zendesk.datasearcher.model.entity;

import com.google.gson.annotations.SerializedName;

public class Ticket extends AbstractEntity {
    private String type;
    private String subject;
    private String description;
    private String priority;
    private String status;
    @SerializedName("submitter_id")
    private String submitterId;
    @SerializedName("assignee_id")
    private String assigneeId;
    @SerializedName("has_incidents")
    private boolean hasIncidents;
    @SerializedName("due_at")
    private String dueAt;
    private String via;
    @SerializedName("organization_id")
    private String organizationId;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(String submitterId) {
        this.submitterId = submitterId;
    }

    public String getAssigneeId() {
        return assigneeId;
    }

    public void setAssigneeId(String assigneeId) {
        this.assigneeId = assigneeId;
    }

    public boolean isHasIncidents() {
        return hasIncidents;
    }

    public void setHasIncidents(boolean hasIncidents) {
        this.hasIncidents = hasIncidents;
    }

    public String getDueAt() {
        return dueAt;
    }

    public void setDueAt(String dueAt) {
        this.dueAt = dueAt;
    }

    public String getVia() {
        return via;
    }

    public void setVia(String via) {
        this.via = via;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    @Override
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Subject: ").append(subject).append("\n")
                .append("Description: ").append(description).append("\n")
                .append("Url: ").append(url);
        return summary.toString();
    }

    @Override
    public String toString() {
        return "_id: " + id + "\n" +
                "created_at: " + createdAt + "\n" +
                "tags: " + tags + "\n" +
                "external_id: " + externalId + "\n" +
                "url: " + url + "\n" +
                "type: " + type + "\n" +
                "subject: " + subject + "\n" +
                "description: " + description + "\n" +
                "priority: " + priority + "\n" +
                "status: " + status + "\n" +
                "submitter_id: " + submitterId + "\n" +
                "assignee_id: " + assigneeId + "\n" +
                "has_incidents=" + hasIncidents + "\n" +
                "due_at: " + dueAt + "\n" +
                "via: " + via + "\n" +
                "organization_id: " + organizationId;
    }
}
