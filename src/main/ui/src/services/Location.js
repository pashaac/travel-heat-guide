const host = 'http://localhost:8080';

const getCity = async (mapRef) => {

    let loc = await getCurrentLocation();
    let lat = loc?.lat;
    let lng = loc?.lng;

    const res = await fetch(`${host}/geolocation?lat=${lat}&lng=${lng}`);
    const json = await res.json();

    console.log({ json })

    const bounds = new window.google.maps.LatLngBounds();
    bounds.extend(new window.google.maps.LatLng(json.northEast.latitude, json.northEast.longitude));
    bounds.extend(new window.google.maps.LatLng(json.southWest.latitude, json.southWest.longitude));


    const rectangle = new window.google.maps.Rectangle({
        strokeColor: "#FF0000",
        fillOpacity: 0.15,
        editable: false,
        draggable: false,
        bounds: bounds,
        map: mapRef.current,
    });


    mapRef.current.fitBounds(bounds);

}

const getCurrentLocation = async () => {


    return new Promise((resolve, reject) => {
        if (navigator.geolocation) {
          navigator.geolocation.getCurrentPosition(
            position => {
              const { latitude: lat, longitude: lng } = position.coords;
              resolve({ lat, lng });
            },
            error => {
              reject(error);
            }
          );
        } else {
          reject(new Error("Geolocation is not supported by your browser"));
        }
      });


}

export {
    getCurrentLocation,
    getCity,
}