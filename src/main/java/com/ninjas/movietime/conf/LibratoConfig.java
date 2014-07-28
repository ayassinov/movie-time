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
public class LibratoConfig {

    @NotEmpty(message = "Email is mandatory")
    private String email;

    @NotEmpty(message = "ApiToken is mandatory")
    private String apiToken;

    @NotEmpty(message = "HostName is mandatory")
    private String hostName;

    @NotNull(message = "Activate should have a value of true or false")
    private boolean activate;

    @NotNull(message = "Interval cannot be null")
    private int interval = 20;

}
