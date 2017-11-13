package ru.ifmo.pashaac.treii.service.data.yandex;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Pavel Asadchiy
 * on 21:04 12.11.17.
 */
public enum YandexCity {

    SAINT_PETERSBURG("Санкт-Петербург", "Sankt-Peterburg");

    private final String yandexCity;
    private final String foursquareCity;

    YandexCity(String yandexCity, String foursquareCity) {
        this.yandexCity = yandexCity;
        this.foursquareCity = foursquareCity;
    }

    public static Optional<YandexCity> valueOfByAddress(String address) {
        return Arrays.stream(YandexCity.values())
                .filter(yaCity -> address.contains(yaCity.yandexCity))
                .findFirst();
    }

    public static Optional<YandexCity> valueOfByFoursquareCity(String foursquareCity) {
        return Arrays.stream(YandexCity.values())
                .filter(yaCity -> yaCity.foursquareCity.equals(foursquareCity))
                .findFirst();
    }

    public String getFoursquareCity() {
        return foursquareCity;
    }
}
