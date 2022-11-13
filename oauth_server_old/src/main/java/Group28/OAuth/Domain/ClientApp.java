package Group28.OAuth.Domain;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "ClientApps")
public class ClientApp {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, name = "clientApp_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToMany(mappedBy = "clientApp", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AuthCode> authCodes;

    @OneToMany(mappedBy = "clientApp", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "clientApp", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AccessToken> accessTokens;

    private Long appSecret;

    private String redirectURL;

    private boolean ageRestriction;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getAppSecret() {
        return appSecret;
    }

    public void setAppSecret(Long appSecret) {
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

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<AuthCode> getAuthCodes() {
        return authCodes;
    }

    public void setAuthCodes(Set<AuthCode> authCodes) {
        this.authCodes = authCodes;
    }

    public Set<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(Set<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
    }
}
