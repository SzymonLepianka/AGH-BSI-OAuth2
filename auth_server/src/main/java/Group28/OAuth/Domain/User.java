package Group28.OAuth.Domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.sql.Date;
import java.util.Set;

@Entity
@Table(name = "Users")
public class User {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(unique = true, name = "user_id")
    private Long id;
    @NotNull
    @Column(unique = true)
    private String username;
    @NotNull
    private String password;
    @NotNull
    @Column(unique = true)
    private String email;
    private String firstName;
    private String surname;
    private Date birthDate;
    private String phoneNumber;
    private Boolean isDeveloper;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<ClientApp> clientApps;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AccessToken> accessTokens;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Permission> permissions;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<AuthCode> authCodes;

    public Long getId() {
        return id;
    }

    public void setId(Long ID) {
        this.id = ID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String name) {
        this.username = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String Password) {
        this.password = Password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String FirstName) {
        this.firstName = FirstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String Surname) {
        this.surname = Surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date BirthDate) {
        this.birthDate = BirthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Boolean getDeveloper() {
        return isDeveloper;
    }

    public void setDeveloper(Boolean developer) {
        isDeveloper = developer;
    }

    public Set<ClientApp> getClientApps() {
        return clientApps;
    }

    public void setClientApps(Set<ClientApp> clientApps) {
        this.clientApps = clientApps;
    }

    public Set<AccessToken> getAccessTokens() {
        return accessTokens;
    }

    public void setAccessTokens(Set<AccessToken> accessTokens) {
        this.accessTokens = accessTokens;
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
}
