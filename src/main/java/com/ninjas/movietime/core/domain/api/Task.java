package com.ninjas.movietime.core.domain.api;

import com.ninjas.movietime.core.util.DateUtils;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * @author ayassinov on 07/09/14.
 */
@Data
@NoArgsConstructor
@ToString
public class Task {

    @Id
    private TaskEnum taskEnum;
    private String cron;
    private Date lastExecutionDate;
    private Date nextExecutionDate;
    private List<TaskExecutionHistory> histories = new ArrayList<>();


    public Task(TaskEnum taskEnum, String cron) {
        this.taskEnum = taskEnum;
        this.cron = cron;
        this.nextExecutionDate = DateUtils.nextCronStartDate(this.cron);
    }

    public String addExecutionHistory(boolean isSucceed, String message) {
        final String uid = UUID.randomUUID().toString();
        histories.add(new TaskExecutionHistory(isSucceed, uid, message, DateUtils.now()));
        return uid;
    }

}
