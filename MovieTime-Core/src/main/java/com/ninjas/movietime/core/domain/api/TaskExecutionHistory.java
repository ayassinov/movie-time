package com.ninjas.movietime.core.domain.api;

import lombok.Data;
import lombok.ToString;

import java.util.Date;

/**
 * @author ayassinov on 07/09/14.
 */
@Data
@ToString
public class TaskExecutionHistory {

    private boolean isSucceed;
    private Date executionDate;

    public TaskExecutionHistory(boolean isSucceed, Date executionDate) {
        this.isSucceed = isSucceed;
        this.executionDate = executionDate;
    }
}
