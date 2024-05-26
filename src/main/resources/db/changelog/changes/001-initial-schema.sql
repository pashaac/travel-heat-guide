CREATE TABLE City (

    id BIGINT AUTO_INCREMENT PRIMARY KEY,

    name VARCHAR(255),
    country_name VARCHAR(255),

    south_west_latitude DOUBLE,
    south_west_longitude DOUBLE,

    north_east_latitude DOUBLE,
    north_east_longitude DOUBLE,

    CONSTRAINT city_country_unique UNIQUE (name, country_name)
);

