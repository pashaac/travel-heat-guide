package ru.ifmo.pashaac.treii;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import ru.ifmo.pashaac.treii.domain.vo.Marker;
import ru.ifmo.pashaac.treii.service.data.google.GoogleService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TreiiApplicationTests {

    private static final Logger logger = LoggerFactory.getLogger(TreiiApplicationTests.class);

    @Autowired
    private GoogleService googleService;

	@Test
	public void contextLoads() {
	}

    @Test
    public void googleGeolocationTest() {
	    logger.info(googleService.geolocation(new Marker(59.939923, 30.314603)).toString());
	    logger.info(googleService.geolocation(new Marker(59.761692, 30.521103)).toString());
	    logger.info(googleService.geolocation(new Marker(59.712553, 30.361801)).toString());
	    logger.info(googleService.geolocation(new Marker(59.872162, 29.871536)).toString());

	    logger.info(googleService.geolocation(new Marker(59.781742, 29.871536)).toString());
    }
}
