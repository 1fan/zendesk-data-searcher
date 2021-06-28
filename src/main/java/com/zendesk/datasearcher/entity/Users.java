package com.zendesk.datasearcher.entity;

import com.google.gson.annotations.SerializedName;

public class Users extends AbstractEntity {

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
    public String toString() {
        return "Users{" +
                "_id='" + id + '\'' +
                ", url='" + url + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", tags=" + tags +
                ", externalId='" + externalId + '\'' +
                ", name='" + name + '\'' +
                ", alias='" + alias + '\'' +
                ", active=" + active +
                ", verified=" + verified +
                ", shared=" + shared +
                ", locale='" + locale + '\'' +
                ", timezone='" + timezone + '\'' +
                ", lastLoginAt='" + lastLoginAt + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", signature='" + signature + '\'' +
                ", organizationId='" + organizationId + '\'' +
                ", suspended=" + suspended +
                ", role='" + role + '\'' +
                '}';
    }
}
