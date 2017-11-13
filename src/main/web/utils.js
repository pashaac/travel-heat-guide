var removeMarkers = function (markers) {
    _.forEach(markers, function (marker) {
        marker.setMap(null);
    });
    _.remove(markers);
};

var removeBoundingBoxes = function (boundingBoxes) {
    _.forEach(boundingBoxes, function (boundingBox) {
        boundingBox.setMap(null);
    });
    _.remove(boundingBoxes);
};

var removeAttractionAreas = function (attractionAreas) {
    _.forEach(attractionAreas, function (attractionCircle) {
        attractionCircle.setMap(null);
    });
    _.remove(attractionAreas);
};

var drawMarkers = function (data, map) {
    var markers = [];
    _.forEach(data, function (place) {
        markers.push(googleMarker(place, map));
    });
    return markers;
};

var drawBoundingBoxes = function (data, map) {
    var boundingBoxes = [];
    _.forEach(data, function (boundingBox) {
        boundingBoxes.push(googleBoundingBox(boundingBox, map));
    });
    return boundingBoxes;
};

var drawWeightBoundingBoxes = function (data, map) {
    var boundingBoxes = [];
    _.forEach(data, function (boundingBox) {
        boundingBoxes.push(googleWeightBoundingBox(boundingBox, map));
    });
    return boundingBoxes;
};

var drawAttractionArea = function (data, map) {
    var attractionCirclesAreas = [];
    _.forEach(data, function (attraction) {
        attractionCirclesAreas.push(googleAttractionArea(attraction, map));
    });
    return attractionCirclesAreas;
};

var drawYandexEstate = function (data, map) {
    var yandexEstates = [];
    _.forEach(data, function (yandexEstate) {
        yandexEstates.push(googleMarkerYandexEstate(yandexEstate, map));
    });
    return yandexEstates;
};



var drawGeolocation = function (data, map) {
    var boundingBoxes = [];
    boundingBoxes.push(googleBoundingBox(data.boundingBoxes[0], map));
    return boundingBoxes;
};