package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.GeoLookup;
import ac.uk.bristol.cs.santa.grotto.business.route.Location;
import ac.uk.bristol.cs.santa.grotto.business.route.LocationRoutePlanning;
import ac.uk.bristol.cs.santa.grotto.rest.GrottoDTO;
import com.google.maps.errors.ApiException;
import com.sun.tools.javac.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

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
