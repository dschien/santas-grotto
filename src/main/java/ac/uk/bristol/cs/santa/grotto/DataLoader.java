package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.UserRepository;
import ac.uk.bristol.cs.santa.grotto.business.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Created by csxds on 01/12/2017.
 * <p>
 * "Script" to run at application start.
 * <p>
 * Fill database with initial state.
 */


@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(DataLoader.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Value("${initial_admin_account.password}")
    private String password;
    @Value("${initial_admin_account.username}")
    private String username;


    public void run(ApplicationArguments args) {


        if (userRepository.findByUsername(username) == null) {
            LOG.debug("creating initial admin account");
            userService.createUser(username, "ROLE_ADMIN", password);
        }
        if (userRepository.findByUsername("user") == null) {
            LOG.debug("creating test user account");
            userService.createUser("user", "USER", "test");
        }

    }
}
