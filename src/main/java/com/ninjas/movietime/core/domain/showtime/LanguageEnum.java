package com.ninjas.movietime.core.domain.showtime;

import lombok.Getter;

/**
 * @author ayassinov on 28/08/2014.
 */
public enum LanguageEnum {
    ENGLISH(""),
    FRENCH(""),
    CHINESE(""),
    TURKISH(""),
    ITALIAN(""),
    DANISH("");

    @Getter
    private final String code;

    private LanguageEnum(String code) {
        this.code = code;
    }

    /*public static LanguageEnum fromCode(final String code){
    }
*/
    @Override
    public String toString() {
        return this.name();
    }
}
