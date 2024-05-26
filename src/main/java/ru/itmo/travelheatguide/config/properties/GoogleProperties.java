package ru.itmo.travelheatguide.config.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;


@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "google")
public class GoogleProperties {

    private String apiKey;
    private int queryRateLimit;

}
