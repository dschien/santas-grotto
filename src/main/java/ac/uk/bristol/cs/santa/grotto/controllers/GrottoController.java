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
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by csxds on 24/11/2017.
 */
@Controller
public class GrottoController {


    private static final Logger LOG = LoggerFactory.getLogger(GrottoController.class);


    @Autowired
    private GrottoRepository grottoRepository;


    @Secured({"ROLE_ADMIN"})
    @GetMapping("/grotto/add")
    public String addGrotto(Model model) {
        model.addAttribute("grotto", new Grotto());
        return "grotto_form";
    }


    @Secured({"ROLE_ADMIN"})
    @PostMapping("/grotto")
    public String submitGrotto(@ModelAttribute Grotto grotto) {
        LOG.info("Saving new grotto with name " + grotto.getName());
        Grotto grotto1 = grottoRepository.save(grotto);
        return "redirect:/grotto/" + grotto1.getId();
    }


    @GetMapping(value = {"/grotto", "/grotto/{grottoId}"})
    public String viewGrotto(@PathVariable Optional<Long> id, Model model) {
        LOG.info("Listing grottos");
        model.addAttribute("grottos", id.map(aLong -> Collections.singletonList(grottoRepository.findById(aLong).get()))
                .orElseGet(() -> grottoRepository.findAll()));
        return "grotto_view";
    }


}
