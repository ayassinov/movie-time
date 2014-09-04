package com.ninjas.movietime.core.domain.movie;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

/**
 * @author ayassinov on 04/09/2014.
 */
@Data
@NoArgsConstructor
@EqualsAndHashCode(of = ("id"))
public class Nationality {

    @Id
    private String id;
    private String name;

    public Nationality(String id, String name) {
        this.id = id;
        this.name = name;
    }
}
