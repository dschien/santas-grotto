package ac.uk.bristol.cs.santa.grotto.business;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Service
public class GeoLookup {

    private GeoApiContext context;

    public GeoLookup(@Value("${external-services.google-maps.api-key}") final String apiKey) {
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    public Pair<Double, Double> latLngFromAddress(String address) throws InterruptedException, ApiException, IOException {

        GeocodingResult[] results = GeocodingApi.geocode(context, address).await();

        return Pair.of(results[0].geometry.location.lat, results[0].geometry.location.lng);
    }

}
