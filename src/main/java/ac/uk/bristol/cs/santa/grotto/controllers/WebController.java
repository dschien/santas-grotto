package ac.uk.bristol.cs.santa.grotto.controllers;

import ac.uk.bristol.cs.santa.grotto.business.GeoLookup;
import ac.uk.bristol.cs.santa.grotto.business.UserRepository;
import ac.uk.bristol.cs.santa.grotto.business.UserService;
import ac.uk.bristol.cs.santa.grotto.business.data.*;

import ac.uk.bristol.cs.santa.grotto.business.dto.AccountDTO;
import ac.uk.bristol.cs.santa.grotto.business.route.Location;
import ac.uk.bristol.cs.santa.grotto.business.route.LocationRoutePlanning;
import ac.uk.bristol.cs.santa.grotto.business.dto.GrottoDTO;
import com.google.maps.errors.ApiException;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by csxds on 24/11/2017.
 */
@Controller
public class WebController extends WebMvcConfigurerAdapter {


    private static final Logger LOG = LoggerFactory.getLogger(WebController.class);

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;


    /**
     * view controllers without logic
     *
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/contact");
        registry.addViewController("/").setViewName("index");
        registry.addViewController("/login");
        registry.addViewController("/terms").setViewName("terms");
    }

    @RequestMapping("/login-error.html")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "login";
    }

    @Autowired
    private GeoLookup geoLookup;


    @RequestMapping(value = "/api/geolookup", method = RequestMethod.POST)
    @ResponseBody
    public String geolookup(@RequestBody String address) throws InterruptedException, ApiException, IOException {
        return String.valueOf(geoLookup.latLngFromAddress(address));
    }

    @Autowired
    private LocationRoutePlanning routePlanning;


    @RequestMapping(value = "/api/grottoroute", method = RequestMethod.POST)
    @ResponseBody
    public String grottoRoute(@RequestBody GrottoDTO[] grottos) throws InterruptedException, ApiException, IOException {
        ArrayList<Location> locations = new ArrayList<>();
        for (GrottoDTO grotto : grottos) {
            Pair<Double, Double> latLong = geoLookup.latLngFromAddress(grotto.getAddress());
            locations.add(new Location(grotto.getName(), latLong.getFirst(), latLong.getSecond()));
        }
        ArrayList<Location> tour = routePlanning.computeOptimalTour(locations, 1000);
        return tour.stream()
                .map(Location::getName)
                .collect(Collectors.joining(","));
    }


    @Autowired
    private ContactRequestRepository contactRepository;


    @GetMapping("/contact")
    public String showContactForm(ContactRequest contact) {
        return "contact";
    }

    @PostMapping(value = "/contact")
    public String submitContact(@Valid ContactRequest contact, BindingResult binding, RedirectAttributes attr) {
        if (binding.hasErrors()) {
            return "/contact";
        }
        contactRepository.save(contact);
        attr.addFlashAttribute("message", "Thank you for your message. We'll be in touch ASAP");
        return "redirect:/contact";
    }


    private ModelMapper modelMapper = new ModelMapper();
//
    @Secured({"ROLE_ADMIN"})
    @PostMapping("/api/account")
    public ResponseEntity<?> create(@RequestBody AccountDTO account) {
        if (account.getPassword() == null) {
            return new ResponseEntity<>("password for account not included in DTO", HttpStatus.BAD_REQUEST);
        }
        User destination = null;
        ResponseEntity response = null;
        if (account.getUserId() != null) {
            Optional<User> oldUser = userRepository.findById(account.getUserId());
            if (oldUser.isPresent()) {
                destination = oldUser.get();
                modelMapper.map(account, destination);
            } else {
                return new ResponseEntity<>("user id not found or password not set", HttpStatus.BAD_REQUEST);
            }
        } else {
            destination = modelMapper.map(account, User.class);
        }

        // save user
        LOG.info("creating/updating user account " + destination.getUsername());
        userService.saveUser(destination);


        return new ResponseEntity<>("OK", HttpStatus.CREATED);
    }

}
