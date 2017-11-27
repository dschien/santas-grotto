package ac.uk.bristol.cs.santa.grotto;


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
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import javax.transaction.Transactional;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Created by csxds on 26/11/2017.
 */

@RunWith(SpringRunner.class)
@SpringBootTest
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class})
@ActiveProfiles("test")
@Transactional
public class GrottoTest {


    @Autowired
    GrottoRepository grottoRepository;

    @Autowired
    EventRepository eventRepository;
    @Autowired
    EventBookingRepository eventBookingRepository;

    @Autowired
    UserRepository userRepository;


//    @Test
//    @DatabaseSetup("/grotto_test.xml")
//    public void basic_test() {
//        Grotto grotto = grottoRepository.findOne(1L);
//        System.out.println(grotto.getName());
////        System.out.println(grotto.getEvents().size());
////        System.out.println(eventRepository.findOne(1L).getId());
//    }

    @Test
    public void createGrottoTest() {

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");
        grottoRepository.save(grotto);

        Grotto g2 = grottoRepository.findOne(1L);
        Assert.assertTrue(grotto.getName().equals(g2.getName()));

    }

    @Test
    public void createGrottoEventTest() {

        Assert.assertTrue(eventRepository.count() == 0L);

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(OffsetDateTime.parse("2017-12-03T10:15:30+01:00"));
        event.setGrotto(grotto);

        grotto.addEvent(event);

        eventRepository.save(event);

        Assert.assertTrue(eventRepository.count() == 1L);
        Assert.assertTrue(eventRepository.count() == grottoRepository.count());

        List<Event> events = grottoRepository.findOne(1L).getEvents();

        Assert.assertTrue(events.size() == 1L);


    }

    @Test
    public void createEventBookingTest() {

        Assert.assertTrue(eventRepository.count() == 0L);

        Grotto grotto = new Grotto();
        grotto.setName("Test Grotto");
        grotto.setAddress("Finland");

        Event event = new Event();
        event.setDate(OffsetDateTime.parse("2017-12-03T10:15:30+01:00"));
        event.setGrotto(grotto);

        grotto.addEvent(event);
        eventRepository.save(event);

        User user = new User();
        user.setUserName("testuser");
        user.setEmail("test@me.com");

        userRepository.save(user);

        EventBooking eventBooking = new EventBooking();
        eventBooking.setVisitors(3);
        eventBooking.setUser(user);
        eventBooking.setEvent(event);

        eventBookingRepository.save(eventBooking);

        Assert.assertTrue(eventBookingRepository.count() == 1L);
        Assert.assertTrue(userRepository.count() == grottoRepository.count());

    }

}
