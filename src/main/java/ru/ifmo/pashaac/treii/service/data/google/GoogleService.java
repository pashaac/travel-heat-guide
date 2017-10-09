package ru.ifmo.pashaac.treii.service.data.google;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.exception.ResourceNotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * Created by Pavel Asadchiy
 * on 23:31 09.10.17.
 */
@Service
public class GoogleService {

    private static final String GOOGLE_API_KEY = "AIzaSyA0bOrpaALYb-6caX32Da5KASA94zAoSik";

    private final GeoApiContext googleGeoApiContext;

    public GoogleService() {
        this.googleGeoApiContext = new GeoApiContext.Builder()
                .apiKey(GOOGLE_API_KEY)
                .queryRateLimit(5)
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build();
    }

    public GeocodingResult geocoding(Marker location) {
        try {
            return GeocodingApi.newRequest(googleGeoApiContext).language("en")
                    .latlng(new LatLng(location.getLatitude(), location.getLongitude())).await()[0];
        } catch (ApiException | InterruptedException | IOException e) {
            throw new ResourceNotFoundException("Error was happened due to geocoding by coordinates "
                    + "(" + location.getLongitude() + ", " + location.getLongitude() + ")", e);
        }
    }

    public GeocodingResult geocoding(String address) {
        try {
            return GeocodingApi.newRequest(googleGeoApiContext).language("en").address(address).await()[0];
        } catch (ApiException | InterruptedException | IOException e) {
            throw new ResourceNotFoundException("Error was happened due to geocoding by address: " + address, e);
        }
    }

    public Optional<String> getAddressComponentTypeLongName(GeocodingResult geocodingResult, AddressComponentType addressComponentType) {
        return Arrays.stream(geocodingResult.addressComponents)
                .filter(address -> Arrays.stream(address.types).anyMatch(type -> type == addressComponentType))
                .findAny().map(addressComponent -> addressComponent.longName);
    }

}
