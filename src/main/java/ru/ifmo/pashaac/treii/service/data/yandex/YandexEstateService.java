package ru.ifmo.pashaac.treii.service.data.yandex;

import com.univocity.parsers.common.ParsingContext;
import com.univocity.parsers.common.processor.ObjectColumnProcessor;
import com.univocity.parsers.tsv.TsvParser;
import com.univocity.parsers.tsv.TsvParserSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import ru.ifmo.pashaac.treii.domain.City;
import ru.ifmo.pashaac.treii.domain.YandexEstate;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.repository.YandexEstateRepository;
import ru.ifmo.pashaac.treii.service.CityService;
import ru.ifmo.pashaac.treii.service.miner.QuadTreeMinerService;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Pavel Asadchiy
 * on 14:08 05.11.17.
 */
@Service
public class YandexEstateService {

    private static final Logger logger = LoggerFactory.getLogger(YandexEstateService.class);
    private static final String PATH_TO_YANDEX_ESTATE = "src/main/resources/all_spb_kazan_ekat_msc_offers.tsv";
    private static final String NULL_FIELD = "null";

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

    public void export(double lat, double lng) {
        City city = cityService.localization(lat, lng);
        YandexCity.valueOfByFoursquareCity(city.getCity()).ifPresent(locationYandexCity -> {
            long start = System.currentTimeMillis();
            logger.info("Yandex estate exporting to PostgreSQL starting...");
            TsvParserSettings settings = new TsvParserSettings();
            settings.setAutoConfigurationEnabled(true);
            settings.selectFields("id", "partner_name", "agency_name", "latitude", "longitude", "unified_address", "total_area"
                    , "rooms", "creation_date", "price", "currency");
            List<YandexEstate> yandexEstates = new ArrayList<>();
            ObjectColumnProcessor rowProcessor = new ObjectColumnProcessor() {
                @Override
                public void rowProcessed(Object[] row, ParsingContext context) {
                    if (!valid(row[0], row[3], row[4], row[5], row[6], row[7], row[8], row[9], row[10])) {
                        logger.warn("Ignore estate: " + Arrays.toString(row));
                        return;
                    }
                    YandexEstate estate = Optional.ofNullable(yandexEstateRepository.findByYeid(Long.valueOf(row[0].toString())))
                            .orElseGet(YandexEstate::new);
                    estate.setYeid(Long.valueOf(row[0].toString()));
                    estate.setLocation(new Marker(Double.parseDouble(row[3].toString()), Double.parseDouble(row[4].toString())));
                    estate.setAddress(row[5].toString());
                    estate.setAdditional("Partner name: " + row[1] + ", agency name: " + row[2] + ", date: " + row[8]);
                    estate.setArea(Double.parseDouble(row[6].toString()));
                    estate.setRooms(Integer.parseInt(row[7].toString()));
                    estate.setPrice(Long.parseLong(row[9].toString()));
                    estate.setCurrency(row[10].toString());

                    YandexCity.valueOfByAddress(estate.getAddress())
                            .filter(addressYandexCity -> addressYandexCity == locationYandexCity)
                            .ifPresent(addressYandexCity -> {
                                logger.debug("Yandex estate {} linked with city: {}", estate, addressYandexCity.getFoursquareCity());
                                estate.setCity(city);
                                yandexEstates.add(estate);
                            });
                }
            };
            settings.setProcessor(rowProcessor);
            new TsvParser(settings).parse(tsvFile());
            logger.info("Yandex estates exporting from {} to RAM was finished in time: {} ms, estates count: {}",
                    PATH_TO_YANDEX_ESTATE, System.currentTimeMillis() - start, yandexEstates.size());
            start = System.currentTimeMillis();
            yandexEstateRepository.save(yandexEstates);
            logger.info("Yandex estate exporting to PostgreSQL was finished in time: {} ms", System.currentTimeMillis() - start);
        });
    }

    private boolean valid(Object... objs) {
        return Arrays.stream(objs)
                .allMatch(obj -> Objects.nonNull(obj) && !String.valueOf(obj).equalsIgnoreCase(NULL_FIELD) && !String.valueOf(obj).equalsIgnoreCase("id"));
    }

    private Reader tsvFile() {
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
