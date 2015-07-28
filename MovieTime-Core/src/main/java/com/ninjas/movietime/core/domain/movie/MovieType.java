package com.ninjas.movietime.core.domain.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ayassinov on 04/09/2014.
 */
@Data
@ToString
@TypeAlias("movieType")
@Document(collection = "movieTypes")
@EqualsAndHashCode(of = "code")
public class MovieType {

    @Id
    private String code;
    private String name;

    public MovieType() {
    }

    public MovieType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
