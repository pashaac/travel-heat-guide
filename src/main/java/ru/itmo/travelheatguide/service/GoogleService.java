package ru.itmo.travelheatguide.service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.errors.ApiException;
import com.google.maps.model.AddressComponentType;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.LatLng;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.itmo.travelheatguide.model.Marker;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class GoogleService {

    private final GeoApiContext googleGeoApiContext;

    public GeocodingResult geocoding(Marker location) {
        try {
            LatLng coordinates = new LatLng(location.latitude(), location.longitude());
            return GeocodingApi.newRequest(googleGeoApiContext).latlng(coordinates).await()[0];
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(String.format("Failed geocode user by coordinates (%s, %s) due to: %s",
                    location.latitude(), location.longitude(), e.getMessage()), e);
        }
    }

    public GeocodingResult geocoding(String address) {
        try {
            return GeocodingApi.newRequest(googleGeoApiContext).address(address).await()[0];
        } catch (ApiException | InterruptedException | IOException e) {
            throw new IllegalStateException(String.format("Failed geocode user by address %s due to: %s",
                    address, e.getMessage()), e);
        }
    }

    public Optional<String> getAddressComponentTypeLongName(GeocodingResult geocodingResult, AddressComponentType addressComponentType) {
        return Arrays.stream(geocodingResult.addressComponents)
                .filter(address -> Arrays.stream(address.types).anyMatch(type -> type == addressComponentType))
                .findAny()
                .map(addressComponent -> addressComponent.longName);
    }

}
