package bgs.oauth_server.domain;

public class ClientApp {

    private Integer clientAppId;
    private User user;
    private Integer appSecret;
    private String redirectURL;
    private boolean ageRestriction;

    public Integer getClientAppId() {
        return clientAppId;
    }

    public void setClientAppId(Integer clientAppId) {
        this.clientAppId = clientAppId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(Integer appSecret) {
        this.appSecret = appSecret;
    }

    public String getRedirectURL() {
        return redirectURL;
    }

    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }

    public boolean isAgeRestriction() {
        return ageRestriction;
    }

    public void setAgeRestriction(boolean ageRestriction) {
        this.ageRestriction = ageRestriction;
    }
}
