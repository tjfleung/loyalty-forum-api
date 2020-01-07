package com.terrence.loyalty.forumapi.domain.location;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
@Setter
public class Location {

    private String location;
    private String latitude;
    private String longitude;
    private String temperature;

    public Location() {

    }

    public Location(String location, String latitude, String longitude, String temperature) {
        this.location = location;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
    }
}
