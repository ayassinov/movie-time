package com.ninjas.movietime.core.domain.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ayassinov on 31/08/14.
 */
@Data
@ToString
@TypeAlias("genre")
@Document(collection = "genres")
@EqualsAndHashCode(of = "code")
public class Genre {

    @Id
    private String code;
    private String name;

    public Genre() {
    }

    public Genre(String code, String name) {
        this.code = code;
        this.name = name;
    }
}
