package ru.itmo.travelheatguide.config;

import com.google.maps.GeoApiContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.itmo.travelheatguide.config.properties.GoogleProperties;

import java.util.concurrent.TimeUnit;

@Configuration
public class GoogleConfig {

    @Bean
    public GeoApiContext geoApiContext(GoogleProperties googleProperties) {
        return new GeoApiContext.Builder()
                .apiKey(googleProperties.getApiKey())
                .queryRateLimit(googleProperties.getQueryRateLimit())
                .connectTimeout(1, TimeUnit.SECONDS)
                .readTimeout(1, TimeUnit.SECONDS)
                .writeTimeout(1, TimeUnit.SECONDS)
                .build();
    }

}
