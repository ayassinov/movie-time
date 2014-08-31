package com.ninjas.movietime.core.domain;

import lombok.Data;

/**
 * @author ayassinov on 31/08/14.
 */
@Data
public class Actor {

    private People people;

    private String character;


    public Actor() {
    }

    public Actor(People people, String character) {
        this.people = people;
        this.character = character;
    }


    public Actor(String name, String job, String headShotImageUrl, String character) {
        this(new People(name, job, headShotImageUrl), character);
    }
}
