package ac.uk.bristol.cs.santa.grotto;


import ac.uk.bristol.cs.santa.grotto.business.data.*;
import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

/**
 * Created by csxds on 26/11/2017.
 */

@RunWith(SpringRunner.class) // junit test runner
@SpringBootTest // read app context
// overwrite default TestExecutionListeners in order to add DbUnitTestExecutionListener
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, // use transactional test execution
        DbUnitTestExecutionListener.class}) // to read datasets from file
@ActiveProfiles("test") // use application-test.yml properties (in-memory DB)
@Transactional // rollback DB in between tests
public class GrottoTest {


    @Autowired
    GrottoRepository grottoRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    EventBookingRepository eventBookingRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    @DatabaseSetup("/grotto_test.xml") // read dataset from file
    public void basic_test() {
        Grotto grotto = grottoRepository.findOne(1L);
        Assert.assertTrue(eventRepository.count() == 1L);
        Assert.assertTrue(eventBookingRepository.count() == 1L);
        Assert.assertTrue(userRepository.count() == 1L);
        Assert.assertTrue(grottoRepository.count() == 1L);
    }

    @Test
    public void createGrottoTest() {

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");
        grottoRepository.save(grotto);

        Grotto g2 = grottoRepository.findOne(1L);
        Assert.assertTrue(grotto.getName().equals(g2.getName()));

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

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(new Date());
        event.setGrotto(grotto);

        grotto.addEvent(event);
        eventRepository.save(event);

        UserAccount userAccount = new UserAccount();
        userAccount.setUserName("testuser");
        userAccount.setEmail("test@me.com");

        userRepository.save(userAccount);

        EventBooking eventBooking = new EventBooking();
        eventBooking.setVisitors(3);
        eventBooking.setUserAccount(userAccount);
        eventBooking.setEvent(event);

        eventBookingRepository.save(eventBooking);

        Assert.assertTrue(eventBookingRepository.count() == 1L);
        Assert.assertTrue(userRepository.count() == grottoRepository.count());

    }

}
