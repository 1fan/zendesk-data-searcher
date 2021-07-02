package com.zendesk.datasearcher.model.entity;

import com.google.gson.annotations.SerializedName;

public class User extends AbstractEntity {

    private String name;
    private String alias;
    private boolean active;
    private boolean verified;
    private boolean shared;
    private String locale;
    private String timezone;
    @SerializedName("last_login_at")
    private String lastLoginAt;
    private String email;
    private String phone;
    private String signature;
    @SerializedName("organization_id")
    private String organizationId;
    private boolean suspended;
    private String role;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public boolean isShared() {
        return shared;
    }

    public void setShared(boolean shared) {
        this.shared = shared;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public String getLastLoginAt() {
        return lastLoginAt;
    }

    public void setLastLoginAt(String lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getOrganizationId() {
        return organizationId;
    }

    public void setOrganizationId(String organizationId) {
        this.organizationId = organizationId;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public String getSummary() {
        StringBuilder summary = new StringBuilder();
        summary.append("Name: ").append(name).append("\n")
                .append("Alias: ").append(alias).append("\n")
                .append("Url: ").append(url);
        return summary.toString();
    }

    @Override
    public String toString() {
        return "id: " + id + "\n" +
                "created_at: " + createdAt + "\n" +
                "tags: " + tags + "\n" +
                "external_id: " + externalId + "\n" +
                "url: " + url + "\n" +
                "name: " + name + "\n" +
                "alias: " + alias + "\n" +
                "active: " + active + "\n" +
                "verified: " + verified + "\n" +
                "shared: " + shared + "\n" +
                "locale: " + locale + "\n" +
                "timezone: " + timezone + "\n" +
                "last_login_at: " + lastLoginAt + "\n" +
                "email: " + email + "\n" +
                "phone: " + phone + "\n" +
                "signature: " + signature + "\n" +
                "organization_id: " + organizationId + "\n" +
                "suspended: " + suspended + "\n" +
                "role: " + role;
    }
}
