package com.ninjas.movietime.integration.helpers;

import lombok.Getter;

/**
 * Represent an URL parameters with two properties name, value
 * Use the static one to return a list with only one parameters
 * The toString is surcharged to build an URL core like name=value.
 */
@Getter
public class Parameter {

    private final String name;
    private final String value;

    public Parameter(final String name, final String value) {
        this.name = name;
        this.value = value;
    }

    @Override
    /***
     * Useful to have a format ready url parameters
     */
    public String toString() {
        return String.format("%s=%s", name, value);
    }
}
