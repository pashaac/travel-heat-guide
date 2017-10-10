package ru.ifmo.pashaac.treii.service.data.foursquare;

import fi.foyt.foursquare.api.FoursquareApi;
import fi.foyt.foursquare.api.FoursquareApiException;
import fi.foyt.foursquare.api.Result;
import fi.foyt.foursquare.api.entities.CompactVenue;
import fi.foyt.foursquare.api.entities.VenuesSearchResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.ifmo.pashaac.treii.domain.BoundingBox;
import ru.ifmo.pashaac.treii.domain.Venue;
import ru.ifmo.pashaac.treii.domain.foursquare.FoursquarePlaceType;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 0:28 10.10.17.
 */
@Service
public class FoursquareService {

    private static final Logger logger = LoggerFactory.getLogger(FoursquareService.class);

    public static final int VENUE_MAX_SEARCH = 30;

    private static final int MINIMAL_PLACE_CHECKINS_COUNT = 100;
    private static final int MINIMAL_PLACE_USERS = 50;

    private static final String FOURSQUARE_CLIENT_ID = "PQDUHRGQ3LRAIQBMC5VJZTR0ZENNBYO03Q1ERFXXG1HI0Y1S";
    private static final String FOURSQUARE_CLIENT_SECRET = "2ARYMHXILVTZXYSLZL5THOPWK1OGGIQW03FFRWYG52OCBMRK";

    private final FoursquareApi foursquareApi;

    public FoursquareService() {
        this.foursquareApi = new FoursquareApi(FOURSQUARE_CLIENT_ID, FOURSQUARE_CLIENT_SECRET, "");
    }

    private Map<String, String> params(BoundingBox boundingBox, String foursquareCategoryIds) {
        Map<String, String> params = new HashMap<>();
        params.put("sw", boundingBox.getSouthWest().getLatitude() + "," + boundingBox.getSouthWest().getLongitude());
        params.put("ne", boundingBox.getNorthEast().getLatitude() + "," + boundingBox.getNorthEast().getLongitude());
        params.put("intent", "browse");
        params.put("categoryId", foursquareCategoryIds);
        return params;
    }

    private Optional<FoursquarePlaceType> getVenueType(CompactVenue venue, String foursquareCategoryIds) {
        return Arrays.stream(venue.getCategories())
                .filter(category -> foursquareCategoryIds.contains(category.getId()))
                .map(category -> FoursquarePlaceType.of(category.getId()))
                .filter(Optional::isPresent).map(Optional::get).findAny();
    }

    private boolean isValidCompactVenueAddress(CompactVenue venue) {
        String address = venue.getLocation().getAddress();
        return StringUtils.isEmpty(address) || Character.isUpperCase(address.charAt(0));
    }

    private boolean isValidCompactVenueCheckins(CompactVenue venue) {
        return venue.getStats().getCheckinsCount() > MINIMAL_PLACE_CHECKINS_COUNT;
    }

    private boolean isValidCompactVenueUsers(CompactVenue venue) {
        return venue.getStats().getUsersCount() > MINIMAL_PLACE_USERS;
    }

    public Optional<List<Venue>> search(BoundingBox boundingBox, String foursquareCategoryIds) {
        try {
            Optional<List<Venue>> venues = apiCall(boundingBox, foursquareCategoryIds);
            if (venues.isPresent()) {
                return venues;
            }
            sleep3000();
            return apiCall(boundingBox, foursquareCategoryIds);
        } catch (FoursquareApiException e) {
            sleep3000();
            try {
                return apiCall(boundingBox, foursquareCategoryIds);
            } catch (FoursquareApiException e1) {
                logger.error("Error with foursquare API call, error {}", e1.getMessage());
                return Optional.empty();
            }
        }
    }

    /**
     * @throws FoursquareApiException when trouble with foursquare API or internet connection
     */
    private Optional<List<Venue>> apiCall(BoundingBox boundingBox, String foursquareCategoryIds) throws FoursquareApiException {
        Result<VenuesSearchResult> venuesSearchResult = foursquareApi.venuesSearch(params(boundingBox, foursquareCategoryIds));
        if (venuesSearchResult.getMeta().getCode() != HttpStatus.OK.value()) {
            logger.error("Foursquare venues search api call return code {}", venuesSearchResult.getMeta().getCode());
            return Optional.empty();
        }
        return Optional.of(Arrays.stream(venuesSearchResult.getResult().getVenues())
                .map(venue -> {
                    Venue fVenue = new Venue(venue);
                    fVenue.setType(getVenueType(venue, foursquareCategoryIds).orElse(null));
                    fVenue.setBoundingBox(boundingBox);
                    fVenue.setValid(isValidCompactVenueAddress(venue) && isValidCompactVenueCheckins(venue)
                            && isValidCompactVenueUsers(venue) && Objects.nonNull(fVenue.getType()));

                    String debugCategoriesStr = Arrays.stream(venue.getCategories())
                            .map(category -> category.getName() + " - " + category.getId())
                            .collect(Collectors.joining("|", "[", "]"));
                    logger.info("Venue: {}, {}, checkins {}, users {}", fVenue.getName(), debugCategoriesStr, fVenue.getCheckinsCount(), fVenue.getUsersCount());

                    return fVenue;
                })
                .collect(Collectors.toList()));
    }

    private void sleep3000() {
        logger.info("Sleep for {} milliseconds and repeat request...", 3000);
        try {
            Thread.sleep(3000);
        } catch (InterruptedException ignored) {
        }
    }
}
