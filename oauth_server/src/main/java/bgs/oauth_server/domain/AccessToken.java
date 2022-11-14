package bgs.oauth_server.domain;

import java.sql.*;

public class AccessToken {
    private Integer accessTokenId;
    private User user;
    private ClientApp clientApp;
    private String scope;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Timestamp expiresAt;
    private boolean revoked;

    public Integer getAccessTokenId() {
        return accessTokenId;
    }

    public void setAccessTokenId(Integer accessTokenId) {
        this.accessTokenId = accessTokenId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Timestamp updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Timestamp getExpiresAt() {
        return expiresAt;
    }

    public void setExpiresAt(Timestamp expiresAt) {
        this.expiresAt = expiresAt;
    }

    public boolean isRevoked() {
        return revoked;
    }

    public void setRevoked(boolean revoked) {
        this.revoked = revoked;
    }
}
