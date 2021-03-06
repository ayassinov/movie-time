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

package com.ninjas.movietime.repository;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.theater.Address;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.geo.GeoPage;
import org.springframework.data.geo.Point;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * @author ayassinov on 16/07/14
 */
public class TheaterRepositoryTest extends BaseTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Before
    public void init() {
        final Theater theater = new Theater("AAA", "TEST Theater", 48.88366, 2.3272,
                new Address("roses street", "Paris", "75014"),
                new TheaterChain("111", "UGC"), null);
        theaterRepository.getMongoTemplate().save(theater);
    }

    @Test
    public void listByGeoLocation() {
        final GeoPage<Theater> theaters = theaterRepository.listByGeoLocation(
                new Point(48.88366, 2.3272),
                new PageRequest(0, 2));
        assertThat(theaters.getContent(), not(empty()));
        assertThat(theaters.getContent().size(), is(2));
    }
}
