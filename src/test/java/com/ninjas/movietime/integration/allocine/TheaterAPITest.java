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

package com.ninjas.movietime.integration.allocine;

import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.GeoLocation;
import com.ninjas.movietime.core.domain.Theater;
import com.ninjas.movietime.integration.BaseAlloCineTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.*;

/**
 * @author ayassinov on 17/07/14
 */

public class TheaterAPITest extends BaseAlloCineTest {

    @Autowired
    private TheaterAPI theaterAPI;

    private final int resultCount = 10;
    private final int radius = 50;

    @Test
    public void testListTheaterZip() {
        final int puteauxZip = 92800;
        final List<Theater> theaters = theaterAPI.findByZip(puteauxZip, radius, resultCount);
        Assert.assertThat(theaters.size(), equalTo(10));

        final Theater ugcDefense = theaters.get(1);
        Assert.assertThat(ugcDefense.getName(), equalTo("UGC Ciné Cité La Défense"));
        Assert.assertThat(ugcDefense.getGeoLocation().getLatitude(), equalTo("48.89076"));
        Assert.assertThat(ugcDefense.getAdr().getCity(), equalTo("Paris - La Défense"));
        Assert.assertThat(ugcDefense.getTheaterChain().getName(), equalTo("UGC"));
        Assert.assertThat(ugcDefense.getShutdown(), nullValue());
    }

    @Test
    public void testListTheaterByGeoLocation() {
        final GeoLocation puteauxLocation = new GeoLocation("48.88288288288288", "2.246771145958308");
        final List<Theater> theaters = theaterAPI.findByGeoLocation(puteauxLocation, radius, resultCount);
        Assert.assertThat(theaters.size(), equalTo(10));

        final Theater leCentral = theaters.get(7);
        Assert.assertThat(leCentral.getName(), equalTo("Mac-Mahon"));
        Assert.assertThat(leCentral.getAdr().getAddress(), equalTo("5, av. Mac-Mahon"));
        Assert.assertThat(leCentral.getTheaterChain().getCode(), equalTo("81007"));
        Assert.assertThat(leCentral.getShutdown(), notNullValue());
        Assert.assertThat(leCentral.getShutdown().getReason(), equalTo("FERMETURE ESTIVALE"));
    }

    @Test
    public void testListTheaterByLocation() {
        final String puteauxCityName = "Puteaux";

        final List<Theater> theaters = theaterAPI.findByLocation(puteauxCityName, radius, resultCount);
        Assert.assertThat(theaters.size(), equalTo(2));

        final Theater leCentral = theaters.get(0);
        Assert.assertThat(leCentral.getName(), equalTo("Le Central"));
        Assert.assertThat(leCentral.getAdr().getPostalCode(), equalTo("92800"));
        Assert.assertThat(leCentral.getTheaterChain().getCode(), equalTo("81007"));
        Assert.assertThat(leCentral.getShutdown(), is(nullValue()));
    }

    @Test
    public void testListTheaterByName() {
        final Optional<Theater> response = theaterAPI.findById("B2619");
        Assert.assertThat(response.isPresent(), is(true));
        Assert.assertThat(response.get().getName(), equalTo("UGC Ciné Cité La Défense"));
    }

    @Test
    public void testFindByName() {
        final String response = theaterAPI.findByName("UGC Ciné Cité", resultCount);
        String expectedTheaterName = "\"name\":\"UGC Ciné Cité La Défense\"";
        checkAlloCineAPIResponseError(response, expectedTheaterName);
    }
}
