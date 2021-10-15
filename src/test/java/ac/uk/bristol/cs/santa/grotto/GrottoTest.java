package ac.uk.bristol.cs.santa.grotto;


import ac.uk.bristol.cs.santa.grotto.business.UserService;
import ac.uk.bristol.cs.santa.grotto.business.data.*;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Created by csxds on 26/11/2017.
 */

@RunWith(SpringRunner.class) // junit test runner
@SpringBootTest // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, // use transactional test execution
        DbUnitTestExecutionListener.class}) // to read datasets from file
@Transactional // rollback DB in between tests
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
public class GrottoTest {


    @Autowired
    GrottoRepository grottoRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventBookingRepository eventBookingRepository;

    @Autowired
    ac.uk.bristol.cs.santa.grotto.business.UserRepository userRepository;

    @Autowired
    private UserService userService;


    @Test
    @DatabaseSetup("/grotto_test.xml") // read dataset from file
    public void basic_test() {
        Optional<Grotto> grotto = grottoRepository.findById(1L);
        if (grotto.isPresent()) {
            Assert.assertTrue(eventRepository.count() == 1L);
            Assert.assertTrue(eventBookingRepository.count() == 1L);
            Assert.assertTrue(userRepository.count() == 1L);
            Assert.assertTrue(grottoRepository.count() == 1L);
        }
    }

    @Test
    public void createGrottoTest() {

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");
        grottoRepository.save(grotto);

        Optional<Grotto> g2 = grottoRepository.findById(1L);
        if (g2.isPresent()) {
            Assert.assertTrue(grotto.getName().equals(g2.get().getName()));
        }

    }

    /**
     * Test that saving new event will cascade to transient grotto
     */
    @Test
    public void cascadeEventSaveTest() {

        Assert.assertTrue(eventRepository.count() == 0L);
        Assert.assertTrue(grottoRepository.count() == 0L);

        Grotto grotto = new Grotto();
        String name = "Test Grotto";
        grotto.setName(name);
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(new Date());
        event.setGrotto(grotto);
        grotto.getEvents().add(event);

        eventRepository.save(event);

        Assert.assertTrue(eventRepository.count() == 1L);
        Assert.assertTrue(eventRepository.count() == grottoRepository.count());

        List<Grotto> grottos = grottoRepository.findByName(name);
        List<Event> events = grottos.get(0).getEvents();

        Assert.assertTrue(events.size() == 1L);

    }

    /**
     * Test that saving new grotto and event with {@link Grotto#addEvent} will cascade to events and fill bidirectional.
     */
    @Test
    public void createGrottoEventWithHelperTest() {

        Assert.assertTrue(eventRepository.count() == 0L);
        Assert.assertTrue(grottoRepository.count() == 0L);

        Grotto grotto = new Grotto();
        String name = "Test Grotto";
        grotto.setName(name);
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(new Date());

        grotto.addEvent(event);

        eventRepository.save(event);

        Assert.assertTrue(eventRepository.count() == 1L);
        Assert.assertTrue(eventRepository.count() == grottoRepository.count());

        List<Grotto> grottos = grottoRepository.findByName(name);
        List<Event> events = grottos.get(0).getEvents();

        Assert.assertTrue(events.size() == 1L);

    }

    @Test
    public void createEventBookingTest() {

        Assert.assertTrue(eventRepository.count() == 0L);
        long userCount = userRepository.count();


        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(new Date());
        event.setGrotto(grotto);

        grotto.addEvent(event);
        eventRepository.save(event);

        User user = userService.createUser("test", "ROLE_CUSTOMER", "test");

        EventBooking eventBooking = new EventBooking();
        eventBooking.setVisitors(3);
        eventBooking.setUser(user);
        eventBooking.setEvent(event);

        eventBookingRepository.save(eventBooking);

        Assert.assertTrue(eventBookingRepository.count() == 1L);
        Assert.assertTrue(userRepository.count() == userCount + 1);

    }

}
