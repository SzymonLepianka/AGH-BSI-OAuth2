package Server.Domain;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "Places")
public class Place {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, name = "place_id")
    private Long id;

    @NotNull
    private String name;

    private Boolean validPlace;
    private Float latitude;
    private Float longitude;
    private Long googleMapsId;
    private Long accumulatedScore = 0L;
    private Long usersVoted = 0L;
    private String description;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<FavoritePlace> favoritePlaces;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PointOfRoute> pointOfRoutes;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<PlaceComment> placeComments;

    @OneToMany(mappedBy = "place", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserPlaceVote> userPlaceVotes;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getValidPlace() {
        return validPlace;
    }

    public void setValidPlace(Boolean validPlace) {
        this.validPlace = validPlace;
    }

    public Float getLatitude() {
        return latitude;
    }

    public void setLatitude(Float latitude) {
        this.latitude = latitude;
    }

    public Float getLongitude() {
        return longitude;
    }

    public void setLongitude(Float longitude) {
        this.longitude = longitude;
    }

    public Long getGoogleMapsId() {
        return googleMapsId;
    }

    public void setGoogleMapsId(Long googleMapsId) {
        this.googleMapsId = googleMapsId;
    }

    public Long getAccumulatedScore() {
        return accumulatedScore;
    }

    public void setAccumulatedScore(Long accumulatedScore) {
        this.accumulatedScore = accumulatedScore;
    }

    public Long getUsersVoted() {
        return usersVoted;
    }

    public void setUsersVoted(Long usersVoted) {
        this.usersVoted = usersVoted;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<FavoritePlace> getFavoritePlaces() {
        return favoritePlaces;
    }

    public void setFavoritePlaces(Set<FavoritePlace> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
    }

    public Set<PointOfRoute> getPointOfRoutes() {
        return pointOfRoutes;
    }

    public void setPointOfRoutes(Set<PointOfRoute> pointOfRoutes) {
        this.pointOfRoutes = pointOfRoutes;
    }

    public Set<PlaceComment> getPlaceComments() {
        return placeComments;
    }

    public void setPlaceComments(Set<PlaceComment> placeComments) {
        this.placeComments = placeComments;
    }

    public Set<UserPlaceVote> getUserPlaceVotes() {
        return userPlaceVotes;
    }

    public void setUserPlaceVotes(Set<UserPlaceVote> userPlaceVotes) {
        this.userPlaceVotes = userPlaceVotes;
    }
}