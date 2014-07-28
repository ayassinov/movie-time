package com.ninjas.movietime.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * @author ayassinov on 25/07/14
 */
@Getter
@EqualsAndHashCode
public class Address {

    private final String address;

    private final String city;

    private final String postalCode;

    public Address(String address, String city, String postalCode) {
        this.address = address;
        this.city = city;
        this.postalCode = postalCode;
    }
}
