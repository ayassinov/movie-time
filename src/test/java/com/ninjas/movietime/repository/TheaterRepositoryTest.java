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
import com.ninjas.movietime.core.domain.theater.Theater;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;


/**
 * @author ayassinov on 16/07/14
 */
public class TheaterRepositoryTest extends BaseTest {

    @Autowired
    private TheaterRepository theaterRepository;

    @Test
    public void test() {
        final List<Theater> theaters = theaterRepository.findAll();
        Assert.assertThat(theaters.size(), Matchers.greaterThan(0));
    }
}
