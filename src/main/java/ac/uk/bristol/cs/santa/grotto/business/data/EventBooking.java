package ac.uk.bristol.cs.santa.grotto.business.data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by csxds on 26/11/2017.
 */
@Entity
public class EventBooking {

    @Id
    @GeneratedValue
    private
    Long id;


    @NotNull
    @ManyToOne
    private
    UserAccount userAccount;


    @NotNull
    @ManyToOne
    private
    Event event;

    @NotNull
    private
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
