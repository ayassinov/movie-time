package com.ninjas.movietime.core.domain;

import com.ninjas.movietime.core.util.DateUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

/**
 * @author ayassinov on 29/08/2014.
 */

@Data
@TypeAlias("updateTracking")
@Document(collection = "updatesTracking")
@EqualsAndHashCode(of = "id")
public class UpdateTracking {

    @Id
    private int id;

    private OperationEnum operation;

    private Date updateDate;

    private boolean status;

    private String message;

    public UpdateTracking() {
    }

    public UpdateTracking(OperationEnum operation, boolean status, String message) {
        this.operation = operation;
        this.updateDate = DateUtils.now();
        this.status = status;
        this.message = message;
    }

    public enum OperationEnum {
        THEATER_UPDATE, SHOWTIME_UPDATE, MOVIE_RATING_UPDATE, MOVIE_INFORMATION_UPDATE
    }
}
