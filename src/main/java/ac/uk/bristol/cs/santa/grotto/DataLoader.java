package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.data.UserAccount;
import ac.uk.bristol.cs.santa.grotto.business.data.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by csxds on 01/12/2017.
 */


@Component
public class DataLoader implements ApplicationRunner {

    private UserRepository userRepository;

    @Autowired
    public DataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void run(ApplicationArguments args) {
        UserAccount s = new UserAccount();
        s.setEmail("test@me.com");
        s.setUserName("user");
        userRepository.save(s);
    }
}
