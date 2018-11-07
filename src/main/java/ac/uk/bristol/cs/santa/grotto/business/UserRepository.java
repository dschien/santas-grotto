package ac.uk.bristol.cs.santa.grotto.business;


import ac.uk.bristol.cs.santa.grotto.business.data.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);

    User findByUsername(String username);
}