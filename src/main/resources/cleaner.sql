-- DELETE ALL DATA FROM TABLE
DELETE FROM venue;
DELETE FROM city;
DELETE FROM yandex_estate;

-- DELETE TABLE
DROP TABLE venue;
DROP TABLE city;
DROP TABLE yandex_estate;

-- SOME SELECTS
SELECT * FROM city
;
SELECT count(*), city.city FROM venue INNER JOIN city ON venue.city = city.id GROUP BY city.city;
SELECT count(*), city.city FROM yandex_estate INNER JOIN city ON yandex_estate.city = city.id GROUP BY city.city;

SELECT count(*), address from yandex_estate WHERE address like '%Санкт-Петербург%' GROUP BY address;
SELECT count(*), address from yandex_estate WHERE address like '%Санкт-Петербург%' and additional like '%Яндекс%' GROUP BY address;

SELECT count(*) from yandex_estate WHERE address like '%Санкт-Петербург%' and additional not like '%Яндекс%';

SELECT * from yandex_estate WHERE address like 'Россия, Санкт-Петербург, Комендантский проспект, 60к1';
SELECT * from yandex_estate;

SELECT count(*), address from yandex_estate where estate_ad_type = 'SALE' GROUP BY address;

