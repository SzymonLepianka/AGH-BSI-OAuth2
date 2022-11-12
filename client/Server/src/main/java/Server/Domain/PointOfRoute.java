package Server.Domain;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity // This tells Hibernate to make a table out of this class
@Table(name = "PointsOfRoute")
public class PointOfRoute {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(unique = true, name = "point_of_route_id")
    private Long id;

    private Long pointNumber;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "place_id", nullable = false)
    private Place place;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPointNumber() {
        return pointNumber;
    }

    public Route getRoute() {
        return route;
    }

    public void setRoute(Route route) {
        this.route = route;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public void setPointNumber(Long pointNumber) {
        this.pointNumber = pointNumber;
    }
}