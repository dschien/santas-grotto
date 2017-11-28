package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.GeoLookup;
import ac.uk.bristol.cs.santa.grotto.business.data.Event;
import ac.uk.bristol.cs.santa.grotto.business.data.EventRepository;
import ac.uk.bristol.cs.santa.grotto.business.data.Grotto;
import ac.uk.bristol.cs.santa.grotto.business.data.GrottoRepository;
import ac.uk.bristol.cs.santa.grotto.business.route.Location;
import ac.uk.bristol.cs.santa.grotto.business.route.LocationRoutePlanning;
import ac.uk.bristol.cs.santa.grotto.rest.GrottoDTO;
import com.google.maps.errors.ApiException;
import com.sun.tools.javac.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Created by csxds on 24/11/2017.
 */
@Controller
public class MainController {

    @Autowired
    private GeoLookup geoLookup;

    @Autowired
    private GrottoRepository grottoRepository;

    @Autowired
    private EventRepository eventRepository;

    @GetMapping("/grotto/add")
    public String addGrotto(Model model) {
        model.addAttribute("grotto", new Grotto());
        return "grotto_form";
    }

    @PostMapping("/grotto")
    public String submitGrotto(@ModelAttribute Grotto grotto) {
        grottoRepository.save(grotto);
        return "grotto_view";
    }

    @GetMapping("/grotto/{id}")
    public String viewGrotto(@PathVariable Long id, Model model) {
        model.addAttribute("grotto", grottoRepository.findOne(id));
        return "grotto_view";
    }


    @GetMapping("/event/add")
    public String addEvent(Model model) {
        model.addAttribute("event", new Event());
        model.addAttribute("grottos", grottoRepository.findAll());
        return "event_form";
    }


    @PostMapping("/event")
    public String submitEvent(@ModelAttribute Event event) {
        eventRepository.save(event);
        return "event_view";
    }

    @GetMapping("/event/{id}")
    public String viewEvent(@PathVariable Long id, Model model) {
        model.addAttribute("event", eventRepository.findOne(id));
        return "event_view";
    }


    @RequestMapping(value = "/geolookup", method = RequestMethod.POST)
    @ResponseBody
    public String geolookup(@RequestBody String address) throws InterruptedException, ApiException, IOException {
        return String.valueOf(geoLookup.latLngFromAddress(address));
    }

    @Autowired
    private LocationRoutePlanning routePlanning;


    @RequestMapping(value = "/grottoroute", method = RequestMethod.POST)
    @ResponseBody
    public String grottoRoute(@RequestBody GrottoDTO[] grottos) throws InterruptedException, ApiException, IOException {
        ArrayList<Location> locations = new ArrayList<>();
        for (GrottoDTO grotto : grottos) {
            Pair<Double, Double> latLong = geoLookup.latLngFromAddress(grotto.getAddress());
            locations.add(new Location(grotto.getName(), latLong.fst, latLong.snd));
        }
        ArrayList<Location> tour = routePlanning.computeOptimalTour(locations, 1000);
        return tour.stream()
                .map(Location::getName)
                .collect(Collectors.joining(","));
    }

}
