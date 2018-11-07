package ac.uk.bristol.cs.santa.grotto.business;


import ac.uk.bristol.cs.santa.grotto.business.data.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private UserRepository userRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,

                       BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;

        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(String uname, String role, String password) {
        User s = new User();

        s.setUsername(uname);
        s.setRole(role);
        s.setEnabled(1);
        s.setPassword(password);
        saveUser(s);

        return s;
    }


    public User saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(1);


        return userRepository.save(user);
    }

}