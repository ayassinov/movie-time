package com.ninjas.movietime.core.domain.movie;

import com.ninjas.movietime.core.domain.People;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 10/09/14.
 */
@Getter
@Setter
public class Staff {


    @DBRef
    private List<People> directors = new ArrayList<>();

    @DBRef
    private List<People> writers = new ArrayList<>();

    @DBRef
    private List<People> actors = new ArrayList<>();

    @DBRef
    private List<People> producers = new ArrayList<>();


}
