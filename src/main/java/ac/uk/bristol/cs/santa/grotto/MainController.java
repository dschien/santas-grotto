package ac.uk.bristol.cs.santa.grotto;

import ac.uk.bristol.cs.santa.grotto.business.GeoLookup;
import com.google.maps.errors.ApiException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;

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

}
