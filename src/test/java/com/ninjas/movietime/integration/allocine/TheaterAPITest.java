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

import com.ninjas.movietime.core.domain.Theater;
import com.ninjas.movietime.integration.BaseAlloCineTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author ayassinov on 17/07/14
 */

public class TheaterAPITest extends BaseAlloCineTest {

    @Autowired
    private TheaterAPI theaterAPI;

    @Test
    public void testListTheaterZip() {
        final List<Theater> theaters = theaterAPI.findAllByRegion(75000, 50, 400);
        Assert.assertThat(theaters.size(), equalTo(10));

        final Theater ugcDefense = theaters.get(1);
        Assert.assertThat(ugcDefense.getName(), equalTo("UGC Ciné Cité La Défense"));
        Assert.assertThat(ugcDefense.getGeoLocation().getLatitude(), equalTo(48.89076));
        Assert.assertThat(ugcDefense.getAdr().getCity(), equalTo("Paris - La Défense"));
        Assert.assertThat(ugcDefense.getTheaterChain().getName(), equalTo("UGC"));
        Assert.assertThat(ugcDefense.getShutdown(), nullValue());
    }

}
