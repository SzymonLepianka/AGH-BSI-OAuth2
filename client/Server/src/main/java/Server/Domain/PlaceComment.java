package Server.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "PlaceComments")
public class PlaceComment {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, name = "place_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;

    private String content;

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Place getPlace() {
        return place;
    }

    public String getContent() {
        return content;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setContent(String content) {
        this.content = content;
    }
}