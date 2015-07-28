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

package com.ninjas.movietime.health;

import com.google.common.base.Preconditions;
import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Component;

/**
 * @author ayassinov
 */
@Slf4j
@Component
public class MongoDbHealth implements HealthIndicator {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public Health health() {
        try {
            Preconditions.checkNotNull(mongoTemplate, "mongoTemplate is null");
            DBObject ping = new BasicDBObject("ping", "1");
            final CommandResult pingCommandResult = mongoTemplate.getDb().command(ping);
            pingCommandResult.throwOnError();
            //ping worked, now getting version
            DBObject serverStatus = new BasicDBObject("serverStatus", "1");
            final CommandResult serverStatusCommandResult = mongoTemplate.getDb().command(serverStatus);
            serverStatusCommandResult.throwOnError();

            final String server = pingCommandResult.getServerUsed().toString();
            final String version = serverStatusCommandResult.get("version").toString();

            Preconditions.checkNotNull(server, "Mongo DB Server address is null");
            Preconditions.checkNotNull(version, "Mongo DB version is null");

            return Health.up()
                    .withDetail("database", "mongodb")
                    .withDetail("server", server)
                    .withDetail("version", version)
                    .build();
        } catch (Exception ex) {
            return Health.down(ex).build();
        }
    }
}
