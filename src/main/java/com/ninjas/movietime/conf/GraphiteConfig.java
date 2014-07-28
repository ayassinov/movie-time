package com.ninjas.movietime.conf;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

/**
 * @author ayassinov on 28/07/2014.
 */
@Getter
@Setter
@ToString
public class GraphiteConfig {

    @NotEmpty(message = "ApiKey is mandatory")
    private String apiKey;

    @NotEmpty(message = "Host is mandatory")
    private String host;

    @NotEmpty(message = "Port is mandatory")
    private int port;

    @NotNull(message = "Activate should have a value of true or false")
    private boolean activate;
}
