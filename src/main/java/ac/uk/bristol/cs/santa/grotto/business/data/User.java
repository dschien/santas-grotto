package ac.uk.bristol.cs.santa.grotto.business.data;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
//    @Column(name = "user_id")
    private Long id;

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
//    @NotEmpty(message = "*Please provide an email")
    private String email;

    @Column(unique=true)
    @NotEmpty(message = "*Please provide an username")
    String username;


    @Column(name = "password")
    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    @JsonIgnore
    private String password;

    @Column(name = "name")
//    @NotEmpty(message = "*Please provide your name")
    private String name;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "enabled")
    private int enabled;

    @Column(name = "role")
    @NotEmpty(message = "*Please provide a role")
    private String role;

    @OneToMany(mappedBy = "user")
    private
    List<EventBooking> eventBooking = new ArrayList<>();

}
