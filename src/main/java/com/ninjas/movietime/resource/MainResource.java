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

import com.ninjas.movietime.core.domain.api.Information;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ayassinov on 11/07/14
 */
@RestController
@RequestMapping(produces = "application/json;utf-8")
public class MainResource {

    private final MainService mainService;

    @Autowired
    public MainResource(MainService mainService) {
        this.mainService = mainService;
    }

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public List<String> manage() throws Exception {
        MetricManager.markResourceMeter("manage", "root");
        return mainService.listManageEndPoints();
    }

    @RequestMapping(value = {"/", "/api", "api/${movietime.app.apiVersion}"}, method = RequestMethod.GET)
    public Information main() throws Exception {
        MetricManager.markResourceMeter("root");
        return mainService.getAppInformation();
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
