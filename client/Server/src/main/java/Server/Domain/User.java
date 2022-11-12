package Server.Domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;
import java.util.Set;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "Users")
public class User {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
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

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FavoritePlace> favoritePlaces;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserPlaceVote> userPlaceVotes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaceComment> placeComments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<RouteComment> routeComments;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Route> routes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserRouteVote> userRouteVotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public Set<FavoritePlace> getFavoritePlaces() {
        return favoritePlaces;
    }

    public void setFavoritePlaces(Set<FavoritePlace> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
    }

    public Set<UserPlaceVote> getUserPlaceVotes() {
        return userPlaceVotes;
    }

    public void setUserPlaceVotes(Set<UserPlaceVote> userPlaceVotes) {
        this.userPlaceVotes = userPlaceVotes;
    }

    public Set<PlaceComment> getPlaceComments() {
        return placeComments;
    }

    public void setPlaceComments(Set<PlaceComment> placeComments) {
        this.placeComments = placeComments;
    }

    public Set<RouteComment> getRouteComments() {
        return routeComments;
    }

    public void setRouteComments(Set<RouteComment> routeComments) {
        this.routeComments = routeComments;
    }

    public Set<Route> getRoutes() {
        return routes;
    }

    public void setRoutes(Set<Route> routes) {
        this.routes = routes;
    }

    public Set<UserRouteVote> getUserRouteVotes() {
        return userRouteVotes;
    }

    public void setUserRouteVotes(Set<UserRouteVote> userRouteVotes) {
        this.userRouteVotes = userRouteVotes;
    }
}