package com.ninjas.movietime.core.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author ayassinov on 31/08/14.
 */
@Data
@TypeAlias("people")
@Document(collection = "peoples")
@EqualsAndHashCode(of = "id")
public class People {

    @Id
    private String id;

    private String name;

    private String job;

    private String headShotImageUrl;

    private boolean isUrlImageAvatar;


    public People() {
    }

    public People(String name, String job, String headShotImageUrl) {
        this.id = name.replace(" ", "_").concat("_").concat(job);
        this.name = name;
        this.job = job;
        this.headShotImageUrl = headShotImageUrl;
        this.isUrlImageAvatar = headShotImageUrl.contains("avatar");
    }

}
