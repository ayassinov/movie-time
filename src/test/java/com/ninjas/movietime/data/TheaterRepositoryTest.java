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

package com.ninjas.movietime.data;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.*;
import com.ninjas.movietime.core.domain.Shutdown;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.springframework.test.util.MatcherAssertionErrors.assertThat;


/**
 * @author ayassinov on 16/07/14
 */
public class TheaterRepositoryTest extends BaseTest {

    @Autowired
    private TheaterRepository theaterRepository;

    private Theater theaterToSave;

    @Before
    public void initialize() {
        this.theaterRepository.deleteAll();
        this.theaterToSave = new Theater("UGC Ciné Cité La Défense",
                new GeoLocation(48.88288288288288, 2.246771145958308),
                new Address("5, av. Mac-Mahon", "Paris - La Défense", "92800"),
                new TheaterChain("UGC", "81007"),
                new Shutdown(new Date(), new Date(), "FERMETURE ESTIVALE"));
    }

    @After
    public void tearDown() {
        theaterRepository.deleteAll();
        assertThat(theaterRepository.findAll().size(), is(0));
    }


    @Test
    public void testDelete() {
        theaterRepository.deleteAll();
        assertThat(theaterRepository.findAll().size(), is(0));
    }

    @Test
    public void testInsert() {
        Theater save = theaterRepository.save(this.theaterToSave);
        assertThat(save.getId(), notNullValue());
        assertThat(theaterRepository.findOne(save.getId()), is(save));
    }

    @Test
    public void testFindByNameLikeIgnoreCaseOrderByNameAsc() {
        final Theater saveA = new Theater();
        BeanUtils.copyProperties(theaterToSave, saveA);
        saveA.setName("A Defense");
        final Theater saveB = new Theater();
        BeanUtils.copyProperties(saveA, saveB);
        saveB.setName("B Defense");

        this.theaterRepository.save(saveA);
        this.theaterRepository.save(saveB);

        assertThat(saveA.getId(), notNullValue());
        assertThat(saveB.getId(), notNullValue());
        assertThat(this.theaterRepository.findAll().size(), is(2));

        List<Theater> list = this.theaterRepository.findByNameLikeIgnoreCaseOrderByNameAsc("DEFENSE");
        assertThat("Like and Ignore case", list.size(), is(2));
        assertThat("Order by ascending", list.get(0), is(saveA));
        assertThat("Order by ascending", list.get(1), is(saveB));
    }

    @Test
    public void testFindByLocationLongitude() {
        this.theaterRepository.save(this.theaterToSave);
        assertThat(this.theaterToSave.getId(), notNullValue());

        List<Theater> list = this.theaterRepository.findByGeoLocationLongitude(2.246771145958308);
        assertThat(list.size(), is(1));
        assertThat(list.get(0), is(this.theaterToSave));
    }

}
