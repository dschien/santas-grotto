package ac.uk.bristol.cs.santa.grotto.business.data;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by csxds on 06/02/2018.
 */
@Entity
public class ContactRequest {

    @Id
    @GeneratedValue
    private Long id;

    @NotEmpty(message = "Please enter your name.")
    String name;
    @NotEmpty(message = "Please let us know your email.")
    @Email
    String email;
    @NotEmpty
    String body;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
