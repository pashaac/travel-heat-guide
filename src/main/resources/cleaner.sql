-- DELETE ALL DATA FROM TABLE
DELETE FROM venue;
DELETE FROM city;
DELETE FROM yandex_estate;

-- DELETE TABLE
DROP TABLE venue;
DROP TABLE city;
DROP TABLE yandex_estate;

-- SOME SELECTS
SELECT count(*) FROM yandex_estate;