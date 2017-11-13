var map, post, user;

var attractionVenueContainer = [];
var nightlifeVenueContainer = [];

var attractionVenueAreaContainer = [];
var nightlifeVenueAreaContainer = [];

var boundingBoxContainer = [];

var yandexEstateContainer = [];

var prefix = 'http://localhost:8080';

var mapController = (function ($) {

    var init = function () {
        map = new google.maps.Map($('#map')[0], {
            zoom: 13,
            center: {lat: post.lat, lng: post.lng}
        });
        user = new google.maps.Marker({
            position: {lat: post.lat, lng: post.lng},
            map: map,
            icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|0000FF',
            title: 'You are here!'
        });
        google.maps.event.clearListeners(map, 'rightclick');
        google.maps.event.addListener(map, 'rightclick', function (e) {
            post.lat = e.latLng.lat();
            post.lng = e.latLng.lng();
            user.setMap(null);
            user.setPosition(e.latLng);
            user.setMap(map);
        });
        // $('.modal-inprogress')[0].active = false;
    };

    return {init: init};
})(jQuery);

var clean = function () {
    removeMarkers(attractionVenueContainer);
    removeMarkers(nightlifeVenueContainer);
    removeMarkers(yandexEstateContainer);

    removeAttractionAreas(attractionVenueAreaContainer);
    removeAttractionAreas(nightlifeVenueAreaContainer);

    removeBoundingBoxes(boundingBoxContainer);
};

var getAttractionVenues = function () {
    // $('.spinner').active = true;
    $.get(prefix + '/attraction?' + $.param(post), function (dataAttraction) {
        attractionVenueContainer = attractionVenueContainer.concat(drawMarkers(dataAttraction, map));
        // $('.spinner').active = false;
    });
};

var getAttractionVenuesBoundingboxes = function () {
    // $('.spinner').active = true;
    $.get(prefix + '/attraction/boundingbox?' + $.param(post), function (dataBoundingBox) {
        boundingBoxContainer = boundingBoxContainer.concat(drawBoundingBoxes(dataBoundingBox, map));
        // $('.spinner').active = false;
    });
};

var getAttractionVenuesGrid = function () {
    // $('.modal-inprogress')[0].active = true;
    $.get(prefix + '/attraction/grid?' + $.param(post), function (dataBoundingBox) {
        boundingBoxContainer = boundingBoxContainer.concat(drawWeightBoundingBoxes(dataBoundingBox, map));
        // $('.modal-inprogress')[0].active = false;
    });
};

var getAttractionVenuesAttractiveness = function () {
    $.get(prefix + '/attraction/attractiveness?' + $.param(post), function (dataAttraction) {
        attractionVenueAreaContainer = attractionVenueAreaContainer.concat(drawAttractionArea(dataAttraction, map));
    });
};


var getNightlifeVenues = function () {
};
var getNightlifeVenuesBoundingboxes = function () {
};
var getNightlifeVenuesGrid = function () {
};
var getNightlifeVenuesAttractiveness = function () {
};


var getYandexEstates = function () {
    // $('.spinner').active = true;
    $.get(prefix + '/estate/yandex?' + $.param(post), function (dataYandexEstate) {
        yandexEstateContainer = yandexEstateContainer.concat(drawYandexEstate(dataYandexEstate, map));
        // $('.spinner').active = false;
    });
};

var getYandexEstatesBoundingboxes = function () {
    $.get(prefix + '/estate/yandex/boundingbox?' + $.param(post), function (dataYandexEstateBoundingBoxes) {
        boundingBoxContainer = boundingBoxContainer.concat(drawBoundingBoxes(dataYandexEstateBoundingBoxes, map));
    });
};

var putExportYandexEstates = function () {
    $.ajax({
        url: prefix + '/estate/yandex/export?' + $.param(post),
        type: 'PUT'
    });
};


var mapRunner = function () { // call to google maps callback
    post = {lat: 59.957570, lng: 30.307946};
    if (navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function (pos) {
            post = {lat: pos.coords.latitude, lng: pos.coords.longitude};
            mapController.init();
        }, function () {
            alert('Your browser does not support geolocation service.\n\nUsing default location: Saint-Petersburg, Russia');
            mapController.init();
        });
    } else {
        alert('Your browser does not support geolocation service.\n\nUsing default location: Saint-Petersburg, Russia');
        mapController.init();
    }
};
