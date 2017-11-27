package ac.uk.bristol.cs.santa.grotto.business.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Created by csxds on 26/11/2017.
 */
@Entity
public class Event {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;


    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Collection<EventBooking> getEventBookings() {
        return eventBookings;
    }

    public Grotto getGrotto() {
        return grotto;
    }

    public void setGrotto(Grotto grotto) {
        this.grotto = grotto;
    }

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    private Grotto grotto;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    private Collection<EventBooking> eventBookings = new ArrayList<>();
}
