package ac.uk.bristol.cs.santa.grotto.business.data;


import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by csxds on 26/11/2017.
 */
@Entity
// Default table name "User" is reserved...
//https://stackoverflow.com/questions/4350874/unable-to-use-table-named-user-in-postgresql-hibernate
@Table(name = "`user`")
public class User {

    @Id
    @NotNull
    String userName;

    @NotNull
    @Email
    String email;


    @OneToMany(mappedBy = "user")
    List<EventBooking> eventBooking = new ArrayList<>();

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public User() {

    }

    public boolean addEventBooking(EventBooking eventBooking) {
        return this.eventBooking.add(eventBooking);
    }

    public boolean removeEventBooking(Object o) {
        return eventBooking.remove(o);
    }

    public List<EventBooking> getEventBooking() {
        return eventBooking;
    }

    public void setEventBooking(List<EventBooking> eventBooking) {
        this.eventBooking = eventBooking;
    }


}
