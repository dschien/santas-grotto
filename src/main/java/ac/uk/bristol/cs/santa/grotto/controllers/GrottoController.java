package ac.uk.bristol.cs.santa.grotto.controllers;

import ac.uk.bristol.cs.santa.grotto.business.data.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

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
    public String viewGrotto(@PathVariable Optional<Long> grottoId, Model model) {
        LOG.info("Listing grottos");
        model.addAttribute("grottos", grottoId.map(aLong -> Collections.singletonList(grottoRepository.findById(aLong).get()))
                .orElseGet(() -> grottoRepository.findAll()));
        return "grotto_view";
    }


}
