package ac.uk.bristol.cs.santa.grotto.controllers;

import ac.uk.bristol.cs.santa.grotto.business.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

/**
 * Created by csxds on 24/11/2017.
 */
@Controller
public class EventController {


    private static final Logger LOG = LoggerFactory.getLogger(EventController.class);


    @Autowired
    private GrottoRepository grottoRepository;

    @Autowired
    private EventRepository eventRepository;


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/event/add")
    public String addEvent(Model model) {
        model.addAttribute("event", new Event());
        List<Grotto> all = grottoRepository.findAll();
        assert all.size() > 0;
        model.addAttribute("grottos", all);
        return "event_form";
    }


    @Secured({"ROLE_ADMIN"})
    @PostMapping("/event")
    public String submitEvent(@ModelAttribute Event event) {
        Event event1 = eventRepository.save(event);
        return "redirect:/event/" + event1.getId();

    }

    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        Optional<Event> event = eventRepository.findById(id);
        event.ifPresent(event1 -> model.addAttribute("event", event1));
        return "event_view";
    }


}
