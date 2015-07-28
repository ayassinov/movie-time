package com.ninjas.movietime.core.domain.movie;

import lombok.Data;

/**
* @author ayassinov on 10/09/14.
*/
@Data
public class Language {
    private String code;
    private String name;

    public Language(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
