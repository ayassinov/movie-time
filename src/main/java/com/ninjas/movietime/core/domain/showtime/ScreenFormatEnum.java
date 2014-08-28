package com.ninjas.movietime.core.domain.showtime;

/**
 * @author ayassinov on 28/08/2014.
 */
public enum ScreenFormatEnum {
    DIGITAL("110002"),
    DIGITAL_3D("110003"),
    IMAX_3D("110004");

    private final String code;

    private ScreenFormatEnum(final String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return this.name();
    }
}
