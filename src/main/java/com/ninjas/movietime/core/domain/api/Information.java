package com.ninjas.movietime.core.domain.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.ToString;

/**
 * @author ayassinov on 08/09/14.
 */
@Getter
@ToString
public class Information {

    private final String name;
    private final String version;
    private final String apiVersion;

    public Information(String name, String version, String apiVersion) {
        this.name = name;
        this.version = version;
        this.apiVersion = apiVersion;
    }

    @JsonProperty("default_path")
    public String getDefaultPath() {
        return String.format("/api/%s", this.apiVersion);
    }

}
