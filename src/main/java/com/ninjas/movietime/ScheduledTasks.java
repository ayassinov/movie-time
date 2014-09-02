/*
 * Copyright 2014 Parisian Ninjas
 *
 * Licensed under the MIT License;
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://opensource.org/licenses/MIT
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ninjas.movietime;

import com.ninjas.movietime.service.IntegrationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

/**
 * @author ayassinov on 02/09/2014.
 */
@Slf4j
@Component
@EnableScheduling
public class ScheduledTasks {

    private final static String CRON_EVERY_WED_SUN_AT_23 = "* 23 * * 3,0 * ";
    private final static String CRON_EVERY_SAT_AT_23 = "* 23 * * 1 * ";

    private final IntegrationService integrationService;

    @Autowired
    public ScheduledTasks(IntegrationService integrationService) {
        this.integrationService = integrationService;
    }

    // @Scheduled(cron = CRON_EVERY_WED_SUN_AT_23)
    public void updateMoviesShowtime() {
        logTaskCall("updateMovieShowtime");
        integrationService.updateShowtime(true);
    }

    //@Scheduled(cron = CRON_EVERY_SAT_AT_23)
    public void updateTheaters() {
        logTaskCall("updateTheaters");
        integrationService.updateTheaters();
    }

    private void logTaskCall(String taskName) {
        log.info("Calling task {}", taskName);
    }
}
