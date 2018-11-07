package ac.uk.bristol.cs.santa.grotto.business.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.IndexColumn;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Created by csxds on 26/11/2017.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class EventBooking {

    @Id
    @GeneratedValue
    private
    Long id;


    @NotNull
    @ManyToOne
    private
    User user;


    @NotNull
    @ManyToOne
    private
    Event event;

    @NotNull
    private
    Integer visitors;



}
