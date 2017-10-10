package ru.ifmo.pashaac.treii;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.pashaac.treii.controller.GeolocationController;
import ru.ifmo.pashaac.treii.domain.City;

/**
 * Created by Pavel Asadchiy
 * on 21:23 10.10.17.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GeolocationControllerTest {

    @Autowired
    private GeolocationController geolocationController;

    @Test
    public void geolocationSaintPetersburgTest() {
        City saintPetersburgCity = geolocationController.geolocation(59.939923, 30.314603);
        Assert.assertEquals(saintPetersburgCity.getCity(), "Sankt-Peterburg");
        Assert.assertEquals(saintPetersburgCity.getCountry(), "Russia");
        Assert.assertEquals(saintPetersburgCity.getSouthWest().getLatitude(), 59.74521590000001, 0.001);
        Assert.assertEquals(saintPetersburgCity.getSouthWest().getLongitude(), 30.090332, 0.001);
        Assert.assertEquals(saintPetersburgCity.getNorthEast().getLatitude(), 60.089675, 0.001);
        Assert.assertEquals(saintPetersburgCity.getNorthEast().getLongitude(), 30.559783, 0.001);
    }


    @Test
    public void geolocationMoscowTest() {
        City moscowCity = geolocationController.geolocation(55.774198, 37.551445);
        Assert.assertEquals(moscowCity.getCity(), "Moskva");
        Assert.assertEquals(moscowCity.getCountry(), "Russia");
    }
}
