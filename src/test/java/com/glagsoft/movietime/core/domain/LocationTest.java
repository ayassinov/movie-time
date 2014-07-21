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

package com.glagsoft.movietime.core.domain;

import com.glagsoft.movietime.BaseTest;
import com.glagsoft.movietime.MovieTimeApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * @author ayassinov on 16/07/14
 */
public class LocationTest extends BaseTest{

    @Test
    public void testEquals() {
        final Location locA = new Location("tata", "sasa");
        final Location locB = new Location("tata", "sasa");
        final Location locC = new Location("titi", "sisi");
        assertThat(locA, equalTo(locB));
        assertThat(locA, not(equalTo(locC)));
    }

}
