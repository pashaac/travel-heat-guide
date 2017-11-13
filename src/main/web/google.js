var googleBoundingBox = function(boundingBox, map) {
    return new google.maps.Rectangle({
        strokeColor: '#454543',
        strokeOpacity: 0.9,
        fillOpacity: 0,
        map: map,
        bounds: {
            north: boundingBox.northEast.latitude,
            south: boundingBox.southWest.latitude,
            east: boundingBox.northEast.longitude,
            west: boundingBox.southWest.longitude
        }
    });
};

var googleWeightBoundingBox = function(boundingBox, map) {
    return new google.maps.Rectangle({
        strokeColor: '#454543',
        strokeOpacity: 0.75,
        strokeWeight: 0.8,
        fillOpacity: 0.75,
        fillColor: '#' + boundingBox.color,
        map: map,
        bounds: {
            north: boundingBox.northEast.latitude,
            south: boundingBox.southWest.latitude,
            east: boundingBox.northEast.longitude,
            west: boundingBox.southWest.longitude
        }
    })
};

var googleAttractionArea = function(attraction, map) {
    return new google.maps.Circle({
        strokeOpacity: 0,
        fillColor: '#' + attraction.attractivenessColor,
        fillOpacity: 0.20,
        map: map,
        center: {lat: attraction.location.latitude, lng: attraction.location.longitude},
        radius: attraction.attractivenessArea
    })
};

var googleMarker = function (place, map) {
    return new google.maps.Marker({
        position: {lat: place.location.latitude, lng: place.location.longitude},
        map: map,
        icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|' + place.iconColor,
        title: '' + place.name
    })
};

var googleMarkerYandexEstate = function (estate, map) {
    var marker = new google.maps.Marker({
        position: {lat: estate.location.latitude, lng: estate.location.longitude},
        map: map,
        icon: 'http://chart.apis.google.com/chart?chst=d_map_pin_letter&chld=%E2%80%A2|' + estate.iconColor,
        title: '' + estate.address
    });
    var content = '<div id="content">'+
        '<div id="siteNotice">'+
        '</div>'+
        '<h1 id="firstHeading" class="firstHeading">' + estate.address + '</h1>'+
        '<div id="bodyContent">'+
        '<p><b>Area:</b> ' + estate.area + 'square meters</p>' +
        '<p><b>Rooms count:</b> ' + estate.rooms + '</p>' +
        '<p><b>Price:</b> ' + estate.price + ' ' + estate.currency + '</p>' +
        '<p><b>Additional info</b>: ' + estate.additional + '</p>' +
        '</div>'+
        '</div>';

    var info = new google.maps.InfoWindow({
        content: content
    });
    marker.addListener('click', function() {
        info.open(map, marker);
    });
    return marker;
};