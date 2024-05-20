import React from "react";

import { getCity } from "../../services/Location";

const MainPage = (props) => {

    let lat = 59.957570;
    let lng = 30.307946;

    const { onStart, mapRef, searchInputRef, autocompleteRef } = props;


    const handlePlaceChanged = () => {

        console.log({ qwe: autocompleteRef.current })
        // if (autocompleteRef.current && mapRef.current) {
        //   const place = autocompleteRef.current.getPlace();
        //   if (!place.geometry) {
        //     console.error('No place data available');
        //     return;
        //   }
    
        //   // Move the map to the selected place
        //   mapRef.current.panTo(place.geometry.location);
        //   mapRef.current.setZoom(14); // Optionally, set the zoom level
        // }
      };


    return (
        <>
            <div id="map"></div>

            <button onClick={() => onStart()}>Start</button>
            {/* <div className="common-content">
                <button className="custom indigo" onClick={ map => clean(map)}>Clean</button>
            </div> */}
            <div className="attraction-content">
                <button className="custom indigo" onClick={() => getCity(mapRef)}>Find me</button>
                <input
                    ref={searchInputRef}
                    type="text"
                    placeholder="Search for a location"
                    onChange={handlePlaceChanged}
                />
                {/* <button className="custom indigo" onClick={(map) => getAttractionVenuesBoundingboxes(map)}>Attraction venues (Bounding boxes)</button> */}
                {/* <button className="custom indigo" onClick={(map) => getAttractionVenuesAttractiveness(map)}>Attraction venues (Attractiveness)</button> */}
                {/* <button className="custom indigo" onClick={(map) => getAttractionVenuesGrid(map)}>Attraction venues (Weight Grid)</button> */}
            </div>
            {/* <div className="nightlife-content">
                <button className="custom indigo" onClick={(map) => getNightlifeVenues(map)}>Nightlife venues</button>
                <button className="custom indigo" onClick={(map) => getNightlifeVenuesBoundingboxes(map)}>Nightlife venues (Bounding boxes)</button>
                <button className="custom indigo" onClick={(map) => getNightlifeVenuesAttractiveness(map)}>Nightlife venues (Attractiveness)</button>
                <button className="custom indigo" onClick={(map) => getNightlifeVenuesGrid(map)}>Nightlife venues (Weight Grid)</button>
            </div>
            <div className="yandex-estate-content">
                <button className="custom indigo" onClick={(map) => getYandexEstates(map)}>Yandex real estate</button>
                <button className="custom indigo" onClick={(map) => putExportYandexEstates(map)}>Yandex real estate (Export)</button>
                <button className="custom indigo" onClick={(map) => getYandexEstatesBoundingboxes(map)}>Yandex real estate (Bounding boxes)</button>
            </div> */}
          </>
    );

}

export default MainPage;