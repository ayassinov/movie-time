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
import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.core.domain.Showtime;
import com.ninjas.movietime.integration.BaseAlloCineTest;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;

/**
 * @author ayassinov on 30/07/2014.
 */
public class ShowTimeAPITest extends BaseAlloCineTest {

    @Autowired
    private ShowtimeAPI auShowtimeAPI;

    @Test
    public void testFindByTheaters() {
        ImmutableList<String> theaterIds = ImmutableList.of("B2619", "B0203", "B0074").asList();
        List<Showtime> showTimes = auShowtimeAPI.findByTheaters(theaterIds,
                Optional.<String>absent(),
                Optional.<Date>absent());

        Assert.assertThat(showTimes.size(), equalTo(3));
    }
}
