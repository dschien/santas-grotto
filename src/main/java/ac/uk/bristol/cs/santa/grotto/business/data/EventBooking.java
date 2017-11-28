package ac.uk.bristol.cs.santa.grotto.business.data;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

/**
 * Created by csxds on 26/11/2017.
 */
@Entity
public class EventBooking {

    @Id
    @GeneratedValue
    Long id;


    @NotNull
    @ManyToOne
    UserAccount userAccount;


    @NotNull
    @ManyToOne
    Event event;

    @NotNull
    Integer visitors;

    public EventBooking() {
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    public Integer getVisitors() {
        return visitors;
    }

    public void setVisitors(Integer visitors) {
        this.visitors = visitors;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
