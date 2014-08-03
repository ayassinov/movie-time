package com.ninjas.movietime.conf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.WriteResultChecking;

import javax.validation.constraints.NotNull;

/**
 * @author ayassinov on 03/08/14.
 */
@Getter
@Setter
@ToString
public class MongoConfig {

    @NotNull(message = "Mongo url configuration should not be null or empty")
    private String url;

    @NotNull(message = "WriteResultChecking should not be null, and need to be one of NONE, LOG, EXCEPTION")
    private WriteResultChecking writeResultChecking;
}
