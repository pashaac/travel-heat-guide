package ru.ifmo.pashaac.treii.domain.foursquare;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Pavel Asadchiy
 * on 22:15 10.10.17.
 */
public enum PlaceType {

    MUSEUM(Stream.of(
            "4bf58dd8d48988d181941735",
            "4bf58dd8d48988d18f941735",
            "559acbe0498e472f1a53fa23",
            "4bf58dd8d48988d190941735",
            "4bf58dd8d48988d192941735",
            "4bf58dd8d48988d191941735").collect(Collectors.joining(","))),
    ART(Stream.of(
            "4bf58dd8d48988d136941735",
            "52e81612bcbc57f1066b79ed",
            "52e81612bcbc57f1066b79ee",
            "5109983191d435c0d71c2bb1",
            "4bf58dd8d48988d182941735").collect(Collectors.joining(","))),
    PARK(Stream.of(
            "4bf58dd8d48988d163941735",
            "52e81612bcbc57f1066b7a28").collect(Collectors.joining(","))),
    PLAZA(Stream.of(
            "4bf58dd8d48988d164941735").collect(Collectors.joining(","))),
    SCULPTURE_GARDEN(Stream.of(
            "4bf58dd8d48988d166941735").collect(Collectors.joining(","))),
    SPIRTUAL_CENTER(Stream.of(
            "4bf58dd8d48988d131941735",
            "52e81612bcbc57f1066b7a40",
            "4bf58dd8d48988d132941735",
            "4bf58dd8d48988d13a941735").collect(Collectors.joining(","))),
    THEATER("4bf58dd8d48988d137941735"),
    FOUNTAIN(Stream.of(
            "56aa371be4b08b9a8d573547",
            "56aa371be4b08b9a8d573562",
            "4bf58dd8d48988d161941735").collect(Collectors.joining(","))),
    GARDEN(Stream.of(
            "4bf58dd8d48988d15a941735",
            "52e81612bcbc57f1066b7a22",
            "52e81612bcbc57f1066b7a23").collect(Collectors.joining(","))),
    PALACE(Stream.of(
            "52e81612bcbc57f1066b7a14").collect(Collectors.joining(","))),
    CASTLE(Stream.of(
            "50aaa49e4b90af0d42d5de11").collect(Collectors.joining(","))),

    NIGHTLIFE_SPOT(Stream.of(
            "4d4b7105d754a06376d81259",
            "4bf58dd8d48988d116941735",
            "52e81612bcbc57f1066b7a0d",
            "56aa371ce4b08b9a8d57356c",
            "4bf58dd8d48988d11e941735",
            "4bf58dd8d48988d119941735",
            "4bf58dd8d48988d120941735",
            "4bf58dd8d48988d11b941735",
            "4bf58dd8d48988d11d941735",
            "4bf58dd8d48988d123941735",
            "50327c8591d4c4b30a586d5d",
            "4bf58dd8d48988d121941735",
            "4bf58dd8d48988d11f941735",
            "4bf58dd8d48988d11a941735",
            "4bf58dd8d48988d1d6941735",
            "4bf58dd8d48988d17c941735").collect(Collectors.joining(",")));

    private static final List<PlaceType> nightLifeSpots = Collections.singletonList(NIGHTLIFE_SPOT);
    private static final List<PlaceType> attractions = Arrays.asList(MUSEUM, ART, PARK, PLAZA, SCULPTURE_GARDEN,
            SPIRTUAL_CENTER, THEATER, FOUNTAIN, GARDEN, PALACE, CASTLE);

    private final String categoryIds;

    PlaceType(final String categoryIds) {
        this.categoryIds = categoryIds;
    }

    public String getCategoryIds() {
        return categoryIds;
    }

    public static List<PlaceType> getNightLifeSpots() {
        return nightLifeSpots;
    }

    public static List<PlaceType> getAttractions() {
        return attractions;
    }

    public static Optional<PlaceType> of(String categoryId) {
        return Arrays.stream(values())
                .filter(fPlaceType -> fPlaceType.categoryIds.contains(categoryId))
                .findAny();
    }

    public static boolean containsOnlyInArg(String categoryId, String foursquareCategoryIds) {
        return foursquareCategoryIds.contains(categoryId);
//      Some improvements, which check that venue contains only in foursquareCategoryIds and not contains in other place types categories
//        return attractions.stream()
//                .flatMap(placeType -> Arrays.stream(placeType.getCategoryIds().split(",")))
//                .filter(placeTypeCategoryId -> !foursquareCategoryIds.contains(placeTypeCategoryId))
//                .noneMatch(categoryId::equals);
    }
}
