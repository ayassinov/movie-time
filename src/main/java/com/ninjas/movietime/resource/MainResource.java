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

package com.ninjas.movietime.resource;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ninjas.movietime.MovieTimeConfig;
import com.ninjas.movietime.core.util.MetricManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ayassinov on 11/07/14
 */
@RestController
@RequestMapping(produces = "application/json;utf-8")
public class MainResource {

    private final ObjectMapper mapper;
    private final MovieTimeConfig config;

    @Autowired
    public MainResource(ObjectMapper mapper, MovieTimeConfig config) {
        this.mapper = mapper;
        this.config = config;
    }

    @RequestMapping(value = {"/", "/api"}, method = RequestMethod.GET)
    public String main() throws Exception {
        MetricManager.markResourceMeter("root");
        final ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("name", "Movie Time");
        objectNode.put("version", config.getApp().getVersion());
        objectNode.put("api", String.format("/api/%s", config.getApp().getApiVersion()));
        return objectNode.toString();
    }

    @RequestMapping(value = "/api/${movietime.app.apiVersion}")
    public String apiDocumentation() {
        //TODO list of apis
        final ObjectNode objectNode = mapper.createObjectNode();
        objectNode.put("version", config.getApp().getApiVersion());
        return mapper.createObjectNode().set("api", objectNode).toString();
    }

    /**
     * for new relic ping
     */
    @RequestMapping(method = RequestMethod.HEAD)
    public String ping() {
        MetricManager.markResourceMeter("ping");
        return "pong";
    }
}
