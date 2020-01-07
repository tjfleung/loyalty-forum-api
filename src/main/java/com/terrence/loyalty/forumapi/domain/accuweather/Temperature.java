package com.terrence.loyalty.forumapi.domain.accuweather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class Temperature {

    private double value;
    private String unit;
}
