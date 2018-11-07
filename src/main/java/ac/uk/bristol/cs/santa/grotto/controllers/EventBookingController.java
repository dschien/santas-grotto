package ac.uk.bristol.cs.santa.grotto.controllers;

import ac.uk.bristol.cs.santa.grotto.business.GeoLookup;
import ac.uk.bristol.cs.santa.grotto.business.data.*;
import ac.uk.bristol.cs.santa.grotto.business.route.Location;
import ac.uk.bristol.cs.santa.grotto.business.route.LocationRoutePlanning;
import ac.uk.bristol.cs.santa.grotto.rest.GrottoDTO;
import com.google.maps.errors.ApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by csxds on 24/11/2017.
 */
@Controller
public class EventBookingController {


    private static final Logger LOG = LoggerFactory.getLogger(EventBookingController.class);

    @Autowired
    private ac.uk.bristol.cs.santa.grotto.business.UserRepository userRepository;


    @Autowired
    private EventRepository eventRepository;


    @GetMapping("/eventbooking/add")
    public String addEventBooking(Model model) {
        model.addAttribute("eventbooking", new EventBooking());
        model.addAttribute("events", eventRepository.findAll());
        return "eventbooking_form";
    }

    @Autowired
    private
    EventBookingRepository eventBookingRepository;

    /**
     * Create booking for logged in user.
     * <p>
     * We get the logged in user from Spring and then look up the UserAccount for this user.
     *
     * @param eventbooking
     * @param principal
     * @param model
     * @return
     */
    @PostMapping("/eventbooking")
    public String submitEventBooking(@ModelAttribute EventBooking eventbooking, Principal principal, Model model) {

        User user = userRepository.findByUsername(principal.getName());
        eventbooking.setUser(user);
        EventBooking eventBooking = eventBookingRepository.save(eventbooking);

        model.addAttribute("eventbooking", eventbooking);
        return "redirect:/eventbooking/" + eventBooking.getId();

    }

    @GetMapping("/eventbooking/{id}")
    public String viewEventBooking(@PathVariable Long id, Model model) {
        Optional<EventBooking> eventBooking = eventBookingRepository.findById(id);
        eventBooking.ifPresent(event1 -> model.addAttribute("eventbooking", event1));

        return "eventbooking_view";
    }


}
