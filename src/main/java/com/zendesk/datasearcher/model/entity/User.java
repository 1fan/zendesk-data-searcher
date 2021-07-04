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
        return "Name: " + name + "\n"
                + "Alias: " + alias + "\n"
                + "Url: " + url;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof User)) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        User user = (User) o;

        if (isActive() != user.isActive()) {
            return false;
        }
        if (isVerified() != user.isVerified()) {
            return false;
        }
        if (isShared() != user.isShared()) {
            return false;
        }
        if (isSuspended() != user.isSuspended()) {
            return false;
        }
        if (getName() != null ? !getName().equals(user.getName()) : user.getName() != null) {
            return false;
        }
        if (getAlias() != null ? !getAlias().equals(user.getAlias()) : user.getAlias() != null) {
            return false;
        }
        if (getLocale() != null ? !getLocale().equals(user.getLocale()) : user.getLocale() != null) {
            return false;
        }
        if (getTimezone() != null ? !getTimezone().equals(user.getTimezone()) : user.getTimezone() != null) {
            return false;
        }
        if (getLastLoginAt() != null ? !getLastLoginAt().equals(user.getLastLoginAt()) : user.getLastLoginAt() != null) {
            return false;
        }
        if (getEmail() != null ? !getEmail().equals(user.getEmail()) : user.getEmail() != null) {
            return false;
        }
        if (getPhone() != null ? !getPhone().equals(user.getPhone()) : user.getPhone() != null) {
            return false;
        }
        if (getSignature() != null ? !getSignature().equals(user.getSignature()) : user.getSignature() != null) {
            return false;
        }
        if (getOrganizationId() != null ? !getOrganizationId().equals(user.getOrganizationId()) : user.getOrganizationId() != null) {
            return false;
        }
        return getRole() != null ? getRole().equals(user.getRole()) : user.getRole() == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getName() != null ? getName().hashCode() : 0);
        result = 31 * result + (getAlias() != null ? getAlias().hashCode() : 0);
        result = 31 * result + (isActive() ? 1 : 0);
        result = 31 * result + (isVerified() ? 1 : 0);
        result = 31 * result + (isShared() ? 1 : 0);
        result = 31 * result + (getLocale() != null ? getLocale().hashCode() : 0);
        result = 31 * result + (getTimezone() != null ? getTimezone().hashCode() : 0);
        result = 31 * result + (getLastLoginAt() != null ? getLastLoginAt().hashCode() : 0);
        result = 31 * result + (getEmail() != null ? getEmail().hashCode() : 0);
        result = 31 * result + (getPhone() != null ? getPhone().hashCode() : 0);
        result = 31 * result + (getSignature() != null ? getSignature().hashCode() : 0);
        result = 31 * result + (getOrganizationId() != null ? getOrganizationId().hashCode() : 0);
        result = 31 * result + (isSuspended() ? 1 : 0);
        result = 31 * result + (getRole() != null ? getRole().hashCode() : 0);
        return result;
    }
}
