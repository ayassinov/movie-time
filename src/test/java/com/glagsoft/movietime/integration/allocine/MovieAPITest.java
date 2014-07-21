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

package com.glagsoft.movietime.integration.allocine;

import com.glagsoft.movietime.integration.BaseAlloCineTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author ayassinov on 17/07/14
 */
public class MovieAPITest extends BaseAlloCineTest {

    @Autowired
    private MovieAPI movieAPI;

    @Test
    public void testGetMovie() throws Exception {
        final String response = movieAPI.findById("143067");
        checkAlloCineAPIResponseError(response, "\"title\":\"Cloud Atlas\"");
    }

    @Test
    public void testSearch() {
        final String response = movieAPI.findByName("atlas", 10);
        checkAlloCineAPIResponseError(response, "\"originalTitle\":\"Cloud Atlas\"");
    }
}
