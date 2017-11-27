package ac.uk.bristol.cs.santa.grotto.business.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by csxds on 26/11/2017.
 */
@Entity
public class Event {
    @Id
    @GeneratedValue
    Long id;

    @NotNull
    OffsetDateTime date;

    public Event() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public OffsetDateTime getDate() {
        return date;
    }

    public void setDate(OffsetDateTime date) {
        this.date = date;
    }

    public Grotto getGrotto() {
        return grotto;
    }

    public void setGrotto(Grotto grotto) {
        this.grotto = grotto;
    }

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    Grotto grotto;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL)
    Collection<EventBooking> eventBookings = new ArrayList<>();
}
