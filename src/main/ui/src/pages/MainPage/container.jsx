import React, { useEffect, useRef, useState } from "react";
import Presenter from "./presenter";
import { loadScript } from "../../services/Utils";

const MainPage = () => {

    const mapRef = useRef(null);
    const searchInputRef = useRef(null);
    const autocompleteRef = useRef(null);

    const [mapLoaded, setMapLoaded] = useState(false);


    useEffect(() => {
        const loadGoogleMapsScript = async () => {
            try {
                await loadScript('https://maps.googleapis.com/maps/api/js?key=AIzaSyBnIPT6tMEuEFikN8wz-WHug6rel57m6IM&libraries=places');
            } catch (error) {
                console.error('Error loading Google Maps script:', error);
            }
        };
    
        loadGoogleMapsScript()
            .then(() => setMapLoaded(true));

      }, []);


    const onStart = () => {

        console.log('init map')

        // Create a new map instance centered at a specific location and with a certain zoom level
        mapRef.current = new window.google.maps.Map(document.getElementById('map'), {
          center: {lat: -34.397, lng: 150.644},
          zoom: 8
        }); 

        if (searchInputRef.current) {
            autocompleteRef.current = new window.google.maps.places.Autocomplete(searchInputRef.current);
            autocompleteRef.current.bindTo('bounds', mapRef.current);
        }

    }

    const props = {
        onStart,
        mapRef,
        searchInputRef,
        autocompleteRef,
    };

    return (
        <Presenter { ...props } />
    )

}

export default MainPage;
