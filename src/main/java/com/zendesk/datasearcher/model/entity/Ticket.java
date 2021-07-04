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
        return "Subject: " + subject + "\n"
                + "Description: " + description + "\n"
                + "Url: " + url;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ticket)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        Ticket ticket = (Ticket) o;

        if (isHasIncidents() != ticket.isHasIncidents()) {
            return false;
        }
        if (getType() != null ? !getType().equals(ticket.getType()) : ticket.getType() != null) {
            return false;
        }
        if (getSubject() != null ? !getSubject().equals(ticket.getSubject()) : ticket.getSubject() != null) {
            return false;
        }
        if (getDescription() != null ? !getDescription().equals(ticket.getDescription()) : ticket.getDescription() != null) {
            return false;
        }
        if (getPriority() != null ? !getPriority().equals(ticket.getPriority()) : ticket.getPriority() != null) {
            return false;
        }
        if (getStatus() != null ? !getStatus().equals(ticket.getStatus()) : ticket.getStatus() != null) {
            return false;
        }
        if (getSubmitterId() != null ? !getSubmitterId().equals(ticket.getSubmitterId()) : ticket.getSubmitterId() != null) {
            return false;
        }
        if (getAssigneeId() != null ? !getAssigneeId().equals(ticket.getAssigneeId()) : ticket.getAssigneeId() != null) {
            return false;
        }
        if (getDueAt() != null ? !getDueAt().equals(ticket.getDueAt()) : ticket.getDueAt() != null) {
            return false;
        }
        if (getVia() != null ? !getVia().equals(ticket.getVia()) : ticket.getVia() != null) {
            return false;
        }
        return getOrganizationId() != null ? getOrganizationId().equals(ticket.getOrganizationId()) : ticket.getOrganizationId() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getType() != null ? getType().hashCode() : 0);
        result = 31 * result + (getSubject() != null ? getSubject().hashCode() : 0);
        result = 31 * result + (getDescription() != null ? getDescription().hashCode() : 0);
        result = 31 * result + (getPriority() != null ? getPriority().hashCode() : 0);
        result = 31 * result + (getStatus() != null ? getStatus().hashCode() : 0);
        result = 31 * result + (getSubmitterId() != null ? getSubmitterId().hashCode() : 0);
        result = 31 * result + (getAssigneeId() != null ? getAssigneeId().hashCode() : 0);
        result = 31 * result + (isHasIncidents() ? 1 : 0);
        result = 31 * result + (getDueAt() != null ? getDueAt().hashCode() : 0);
        result = 31 * result + (getVia() != null ? getVia().hashCode() : 0);
        result = 31 * result + (getOrganizationId() != null ? getOrganizationId().hashCode() : 0);
        return result;
    }
}
