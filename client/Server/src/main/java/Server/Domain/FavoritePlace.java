package Server.Domain;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;


@Entity
@Table(name = "FavouritePlaces")
public class FavoritePlace {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, name = "favourite_place_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "place_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Place place;

    public Long getID() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Place getPlace() {
        return place;
    }

    public void setID(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}