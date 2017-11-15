package ru.ifmo.pashaac.treii.service.data.yandex;

import java.util.Arrays;
import java.util.Optional;

/**
 * Created by Pavel Asadchiy
 * on 21:04 12.11.17.
 */
public enum YandexCity {

    MOSCOW("Москва", "Moskva"),                             // 12.3 млн
    SAINT_PETERSBURG("Санкт-Петербург", "Sankt-Peterburg"), // 5.2  млн
    NOVOSIBIRSK("Новосибирск", "Novosibirsk"),              // 1.6  млн
    YEKATERINBURG("Екатеринбург", "Yekaterinburg"),         // 1.4  млн
    NIZHNY_NOVGOROD("Нижний Новгород", "Nizhnij Novgorod"), // 1.2  млн
    KAZAN("Казань", "Kazan'"),                              // 1.2  млн
    CHELYABINSK("Челябинск", "Chelyabinsk"),                // 1.2  млн
    OMSK("Омск", "Omsk"),                                   // 1.1  млн
    SAMARA("Самара", "Samara"),                             // 1.1  млн
    ROSTOV_ON_DON("Ростов-на-Дону", "Rostov"),              // 1.1  млн
    UFA("Уфа", "Ufa"),                                      // 1.1  млн
    KRASNOYARSK("Красноярск", "Krasnoyarsk"),               // 1.0  млн
    PERM("Пермь", "Perm'"),                                 // 1.0  млн
    VORONEZH("Воронеж", "Voronez"),                         // 1.0  млн
    VOLGOGRAD("Волгоград", "Volgograd");                    // 1.0  млн

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

    public String getYandexCity() {
        return yandexCity;
    }
}
