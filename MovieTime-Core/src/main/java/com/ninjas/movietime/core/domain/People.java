package com.ninjas.movietime.core.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ninjas.movietime.core.util.StringUtils;
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
    private JobEnum job;
    private String headShotImageUrl;
    private String character;

    public People() {
    }

    public People(String name, JobEnum job, String headShotImageUrl) {
        this(name, job, headShotImageUrl, null);
    }

    public People(String name, JobEnum job, String headShotImageUrl, String character) {
        this.id = name.replace(" ", "_").toUpperCase().concat("_").concat(job.name());
        this.name = name;
        this.job = job;
        this.headShotImageUrl = headShotImageUrl;
        this.character = character;
    }

    @JsonProperty
    public boolean isUrlImageAvatar() {
        return StringUtils.isNullOrEmpty(headShotImageUrl) || headShotImageUrl.contains("avatar");
    }

    public enum JobEnum {
        DIRECTOR,
        WRITER,
        PRODUCER,
        ACTOR
    }

}
