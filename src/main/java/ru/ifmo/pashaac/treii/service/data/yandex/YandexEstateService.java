package ru.ifmo.pashaac.treii.service.data.yandex;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectColumnProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.YandexEstate;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.domain.yandex.EstateAdType;
import ru.ifmo.pashaac.treii.repository.YandexEstateRepository;
import ru.ifmo.pashaac.treii.service.CityService;
import ru.ifmo.pashaac.treii.service.miner.QuadTreeMinerService;

import javax.annotation.Nullable;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Created by Pavel Asadchiy
 * on 14:08 05.11.17.
 */
@Service
public class YandexEstateService {

    private static final Logger logger = LoggerFactory.getLogger(YandexEstateService.class);

    private static final String PATH_TO_YANDEX_ESTATE = "src/main/resources/yandex_real_estate.csv";
    private static final String NULL_FIELD = "null";
    private static final List<String> NECESSARY_FIELDS = Arrays.asList("id", "partner_name", "agency_name", "latitude",
            "longitude", "unified_address", "total_area", "rooms", "creation_date", "price", "currency", "type", "offer_url", "built_year");

    private YandexEstateRepository yandexEstateRepository;
    private CityService cityService;
    private QuadTreeMinerService quadTreeMinerService;

    public YandexEstateService(YandexEstateRepository yandexEstateRepository, CityService cityService, QuadTreeMinerService quadTreeMinerService) {
        this.yandexEstateRepository = yandexEstateRepository;
        this.cityService = cityService;
        this.quadTreeMinerService = quadTreeMinerService;
    }

    public List<YandexEstate> getYandexEstates(double lat, double lng) {
        return cityService.localization(lat, lng).getYandexEstate();
    }

    @SuppressWarnings("ConstantConditions")
    public void export(double lat, double lng) {
        City city = cityService.localization(lat, lng);
        YandexCity.valueOfByFoursquareCity(city.getCity()).ifPresent(locationYandexCity -> {
            long start = System.currentTimeMillis();
            logger.info("Yandex estate exporting to PostgreSQL starting for city {} ...", locationYandexCity.getYandexCity());
            CsvParserSettings settings = new CsvParserSettings();
            settings.setAutoConfigurationEnabled(true);
            settings.selectFields(NECESSARY_FIELDS.toArray(new String[0]));
            List<YandexEstate> yandexEstates = new ArrayList<>();
            final int[] skippedYandexRealEstates = {0};
            ObjectColumnProcessor rowProcessor = new ObjectColumnProcessor() {
                @Override
                public void rowProcessed(Object[] row, ParsingContext context) {
//                    if ("id".equalsIgnoreCase(String.valueOf(row[0]))) {
//                        logger.info(Arrays.toString(row));
//                    } else {
//                        ++skippedYandexRealEstates[0];
//                        for (Object o : row) {
//                            if (String.valueOf(o).equals("Россия, Санкт-Петербург, Комендантский проспект, 60к1")) {
//                                logger.info(Arrays.toString(row));
//                            }
//                        }
//                        return;
//                    }

                    Long id = getLongValueNoException(row[0]);
                    String partnerName = String.valueOf(row[1]);
                    String agencyName = String.valueOf(row[2]);
                    Double latitude = getDoubleValueNoException(row[3]);
                    Double longitude = getDoubleValueNoException(row[4]);
                    String address = String.valueOf(row[5]);
                    Double area = getDoubleValueNoException(row[6]);
                    Long rooms = getLongValueNoException(row[7]);
                    String date = String.valueOf(row[8]);
                    Long price = getLongValueNoException(row[9]);
                    String currency = String.valueOf(row[10]);
                    Long estateAdType = getLongValueNoException(row[11]);
                    String url = String.valueOf(row[12]);
                    Long buildYear = getLongValueNoException(row[13]);

                    Optional<YandexCity> necessaryCity = YandexCity.valueOfByAddress(address)
                            .filter(addressYandexCity -> addressYandexCity == locationYandexCity);
                    if (!necessaryCity.isPresent()) {
                        return; // other city
                    }
                    boolean validEstate = Stream.<Object>of(id, partnerName, latitude, longitude, address, area,
                            rooms, date, price, currency, estateAdType).noneMatch(obj -> NULL_FIELD.equalsIgnoreCase(String.valueOf(obj)))
                            && Objects.isNull(getDoubleValueNoException(currency)); // not a number
                    if (!validEstate) {
                        ++skippedYandexRealEstates[0];
                        return;
                    }

                    YandexEstate estate = Optional.ofNullable(yandexEstateRepository.findByYeid(id)).orElseGet(YandexEstate::new);
                    estate.setYeid(id);
                    estate.setLocation(new Marker(latitude, longitude));
                    estate.setAddress(address);
                    estate.setAdditional(String.format("partnerName: %s, agencyName: %s, lastUpdated: %s, estateUrl: %s", partnerName, agencyName, date, url));
                    estate.setArea(area);
                    estate.setRooms(rooms.intValue());
                    estate.setPrice(price);
                    estate.setCurrency(currency);
                    estate.setEstateAdType(EstateAdType.valueOfByCode(estateAdType.intValue()));
                    estate.setLastUpdated(getDateValueNoException(date));
                    estate.setBuildYear(Optional.ofNullable(buildYear).orElse(null));

                    logger.debug("Yandex estate {} linked with city: {}", estate, necessaryCity.get().getFoursquareCity());
                    estate.setCity(city);
                    yandexEstates.add(estate);
                }
            };
            settings.setProcessor(rowProcessor);
            new CsvParser(settings).parse(csvFile());
            logger.info("Yandex estates exporting from {} for city {} to RAM was finished in time: {} ms, estates count: {}, was skipped: {} estates",
                    PATH_TO_YANDEX_ESTATE, locationYandexCity.getYandexCity(), System.currentTimeMillis() - start, yandexEstates.size(), skippedYandexRealEstates[0]);
            start = System.currentTimeMillis();
            yandexEstateRepository.save(yandexEstates);
            logger.info("Yandex estate exporting for city {} to PostgreSQL was finished in time: {} ms, was skipped: {} estates", locationYandexCity.getYandexCity(),
                    System.currentTimeMillis() - start, skippedYandexRealEstates[0]);
        });
    }

    @Nullable
    private Date getDateValueNoException(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    @Nullable
    private Long getLongValueNoException(@Nullable Object value) {
        try {
            return Long.parseLong(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    @Nullable
    private Double getDoubleValueNoException(@Nullable Object value) {
        try {
            return Double.parseDouble(String.valueOf(value));
        } catch (NumberFormatException ignored) {
            return null;
        }
    }

    private Reader csvFile() {
        try {
            return new FileReader(PATH_TO_YANDEX_ESTATE);
        } catch (FileNotFoundException e) {
            logger.error("Could't open file by path: " + PATH_TO_YANDEX_ESTATE);
            throw new RuntimeException("Could't open file by path: " + PATH_TO_YANDEX_ESTATE + ", msg: " + e.getMessage());
        }
    }

    public List<BoundingBox> reverseBoundingBoxing(double lat, double lng) {
        City city = cityService.localization(lat, lng);
        List<Marker> reverseYandexEstates = city.getYandexEstate().stream()
                .map(YandexEstate::getLocation)
                .collect(Collectors.toList()); // 1375 work correctly, 1350 doesn't work
        return quadTreeMinerService.reverseMine(city.getBoundingBox(), reverseYandexEstates, 1375);
    }
}
