package com.ninjas.movietime.core.domain.movie;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Strings;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.PersistenceConstructor;

/**
 * @author ayassinov on 10/09/14.
 */
@Getter
@ToString
public class Image {

    private final String url;
    private final ImageTypeEnum type;

    @PersistenceConstructor
    public Image(final String url, final ImageTypeEnum type) {
        this.url = url;
        this.type = type;
    }

    @JsonProperty("isExist")
    public boolean isExist() {
        return !Strings.isNullOrEmpty(url);
    }

    public enum ImageTypeEnum {
        FRENCH_POSTER, ORIGINAL_POSTER, FAN_ART
    }
}
