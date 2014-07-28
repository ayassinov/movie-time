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

package com.ninjas.movietime.integration;

import com.ninjas.movietime.BaseTest;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;

/**
 * @author ayassinov on 17/07/14
 */

public abstract class BaseAlloCineTest extends BaseTest {

    protected void checkAlloCineAPIResponseError(String response, String responseContains) {
        assertThat(response, not(containsString("403")));
        assertThat(response, not(containsString("{\"error\":")));
        assertThat(response, containsString(responseContains));
    }
}
