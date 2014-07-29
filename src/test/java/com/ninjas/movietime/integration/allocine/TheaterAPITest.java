/*
 * Copyright 2014 GlagSoftware
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

package com.ninjas.movietime.integration.allocine;

import com.ninjas.movietime.core.domain.GeoLocation;
import com.ninjas.movietime.integration.BaseAlloCineTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ayassinov on 17/07/14
 */

public class TheaterAPITest extends BaseAlloCineTest {

    @Autowired
    private TheaterAPI theaterAPI;

    private final String expectedTheaterName = "\"name\":\"UGC Ciné Cité La Défense\"";
    private final int resultCount = 10;
    private final int radius = 50;

    @Test
    public void testListTheaterZip() {
        final int puteauxZip = 92800;
        final String response = theaterAPI.findByZip(puteauxZip, radius, resultCount);
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }

    @Test
    public void testListTheaterByGeoLocation() {
        final GeoLocation puteauxLocation = new GeoLocation("48.88288288288288", "2.246771145958308");
        final String response = theaterAPI.findByGeoLocation(puteauxLocation, radius, resultCount);
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }

    @Test
    public void testListTheaterByLocation() {
        final String puteauxCityName = "Puteaux";
        final String response = theaterAPI.findByLocation(puteauxCityName, radius, resultCount);
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }

    @Test
    public void testListTheaterByName() {
        final String response = theaterAPI.findById("B2619", resultCount);
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }

    @Test
    public void testFindByName() {
        final String response = theaterAPI.findByName("UGC Ciné Cité", resultCount);
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }
}
