package Group28.OAuth.Domain;

import javax.persistence.*;

@Entity
@Table(name = "Permissions")
public class Permission {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(unique = true, name = "permission_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "clientApp_id", nullable = false)
    private ClientApp clientApp;

    @ManyToOne
    @JoinColumn(name = "scope_id", nullable = false)
    private Scope scope;

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

    public ClientApp getClientApp() {
        return clientApp;
    }

    public void setClientApp(ClientApp clientApp) {
        this.clientApp = clientApp;
    }

    public Scope getScope() {
        return scope;
    }

    public void setScope(Scope scope) {
        this.scope = scope;
    }
}
