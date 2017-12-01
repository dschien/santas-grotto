package ac.uk.bristol.cs.santa.grotto.business.data;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by csxds on 26/11/2017.
 */
@Repository
public interface UserRepository extends CrudRepository<UserAccount, Long> {

    UserAccount findByUserName(String username);

}

