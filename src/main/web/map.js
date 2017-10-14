var map, post, user, markerContainer = [], boundingBoxContainer = [], attractionAreaContainer = [];

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
        $('.modal-inprogress')[0].active = false;
    };

    var clean = function () {
        removeMarkers(markerContainer);
        removeAttractionAreas(attractionAreaContainer);
        removeBoundingBoxes(boundingBoxContainer);
    };

    var load = function (map) {
        clean();
        var attractionsCheckbox = document.getElementById("attractions-checkbox");
        if (attractionsCheckbox.checked === true) {
            $('.modal-inprogress')[0].active = true;
            var attractionsUrl = createAttractionDataUrl('http://localhost:8080');
            $.get(attractionsUrl, function (dataAttraction) {
                markerContainer = markerContainer.concat(drawMarkers(dataAttraction, map))
                $('.modal-inprogress')[0].active = false;
            });
        }
        var nighLifeSpotsCheckbox = document.getElementById("nightLifeSpots-checkbox");
        if (nighLifeSpotsCheckbox.checked === true) {
            $('.modal-inprogress')[0].active = true;
            var nightLifeSpotUrl = createNightLifeSpotDataUrl('http://localhost:8080');
            $.get(nightLifeSpotUrl, function (dataNightLifeSpots) {
                markerContainer = markerContainer.concat(drawMarkers(dataNightLifeSpots, map))
                $('.modal-inprogress')[0].active = false;
            });
        }
        var boundingboxesCheckbox = document.getElementById("boundingboxes-checkbox");
        if (boundingboxesCheckbox.checked === true) {
            $('.modal-inprogress')[0].gactive = true;
            if (attractionsCheckbox.checked === true) {
                var attractionBoundingboxesUrl = createAttractionBoundingBoxDataUrl('http://localhost:8080');
                $.get(attractionBoundingboxesUrl, function (dataBoundingBox) {
                    boundingBoxContainer = boundingBoxContainer.concat(drawBoundingBoxes(dataBoundingBox, map));
                    $('.modal-inprogress')[0].active = false;
                });
            }
            if (nighLifeSpotsCheckbox.checked === true) {
                var nighLifeSpotsBoundingboxesUrl = createNightLifeSpotBoundingBoxDataUrl('http://localhost:8080');
                $.get(nighLifeSpotsBoundingboxesUrl, function (dataBoundingBox) {
                    boundingBoxContainer = boundingBoxContainer.concat(drawBoundingBoxes(dataBoundingBox, map));
                    $('.modal-inprogress')[0].active = false;
                });
            }
        }
        var attractivenessCheckbox = document.getElementById("attractiveness-checkbox");
        if (attractivenessCheckbox.checked === true) {
            $('.modal-inprogress')[0].active = true;
            var attractivenessUrl = createMlAttractionDataUrl('http://localhost:8080');
            $.get(attractivenessUrl, function (dataAttraction) {
                attractionAreaContainer = drawWeightBoundingBoxes(dataAttraction, map);
                $('.modal-inprogress')[0].active = false;
            });
        }
    };

    var createAttractionDataUrl = function (base) {
        return base + '/data/attraction?' + $.param(post);
    };
    var createNightLifeSpotDataUrl = function (base) {
        return base + '/data/nightLifeSpot?' + $.param(post);
    };
    var createAttractionBoundingBoxDataUrl = function (base) {
        return base + '/data/attraction/boundingbox?' + $.param(post);
    };
    var createNightLifeSpotBoundingBoxDataUrl = function (base) {
        return base + '/data/nightLifeSpot/boundingbox?' + $.param(post);
    };
    var createMlAttractionDataUrl = function (base) {
        return base + '/ml/grid?' + $.param(post);
    };
    return {clean: clean, load: load, init: init};
})(jQuery);

var mapRunner = function () {
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
