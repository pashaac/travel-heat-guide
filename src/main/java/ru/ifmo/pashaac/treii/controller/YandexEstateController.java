package ru.ifmo.pashaac.treii.controller;

import io.swagger.annotations.ApiModel;
import org.springframework.web.bind.annotation.*;
import ru.ifmo.pashaac.treii.domain.YandexEstate;
import ru.ifmo.pashaac.treii.domain.vo.BoundingBox;
import ru.ifmo.pashaac.treii.service.data.yandex.YandexEstateService;

import java.util.List;

/**
 * Created by Pavel Asadchiy
 * on 15:29 05.11.17.
 */
@RestController
@RequestMapping("/estate/yandex")
@ApiModel(value = "Yandex real estate manager controller", description = "API for work with project 'Yandex real estate' resources")
@CrossOrigin
public class YandexEstateController {

    private final YandexEstateService yandexEstateService;

    public YandexEstateController(YandexEstateService yandexEstateService) {
        this.yandexEstateService = yandexEstateService;
    }

    @RequestMapping(value = "/export", method = RequestMethod.PUT)
    public void export(@RequestParam double lat, @RequestParam double lng) {
        yandexEstateService.export(lat, lng);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public List<YandexEstate> yandexEstates(@RequestParam double lat, @RequestParam double lng) {
        return yandexEstateService.getYandexEstates(lat, lng);
    }

    @RequestMapping(value = "/boundingbox", method = RequestMethod.GET)
    public List<BoundingBox> yandexEstatesBoundingBoxes(@RequestParam double lat, @RequestParam double lng) {
        return yandexEstateService.reverseBoundingBoxing(lat, lng);
    }

}
