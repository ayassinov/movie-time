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

package com.ninjas.movietime.data;

import com.ninjas.movietime.BaseTest;
import com.ninjas.movietime.core.domain.Location;
import com.ninjas.movietime.core.domain.Theater;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.OutputCapture;

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

    @Rule
    public OutputCapture outputCapture = new OutputCapture();


    @Before
    public void init() {
        theaterRepository.deleteAll();
    }


    @Test
    public void testDelete() {
        theaterRepository.deleteAll();
        assertThat(theaterRepository.findAll().size(), is(0));
    }

    @Test
    public void testInsert() {
        Theater save = theaterRepository.save(new Theater("La defense", new Location("A", "B")));
        assertThat(save.getId(), notNullValue());
        assertThat(theaterRepository.findOne(save.getId()), is(save));
    }

    @Test
    public void testFindByNameLikeIgnoreCaseOrderByNameAsc() {
        Theater saveA = theaterRepository.save(new Theater("A Defense", new Location("A", "B")));
        Theater saveZ = theaterRepository.save(new Theater("Z defense", new Location("D", "E")));
        assertThat(saveA.getId(), notNullValue());
        assertThat(saveZ.getId(), notNullValue());
        assertThat(theaterRepository.findAll().size(), is(2));

        List<Theater> list = theaterRepository.findByNameLikeIgnoreCaseOrderByNameAsc("defense");
        assertThat(list.size(), is(2));
        assertThat(list.get(0), is(saveA));
        assertThat(list.get(1), is(saveZ));
    }

    @Test
    public void testFindByLocationLongitude() {
        Theater saveA = theaterRepository.save(new Theater("A Defense", new Location("A", "B")));
        Theater saveZ = theaterRepository.save(new Theater("Z defense", new Location("D", "E")));

        assertThat(saveA.getId(), notNullValue());
        assertThat(saveZ.getId(), notNullValue());
        
        assertThat(theaterRepository.findAll().size(), is(2));

        List<Theater> list = theaterRepository.findByLocationLongitude("A");
        // TODO : to fix return 0 and should be 1
        //assertThat(list.size(), is(1));
        //assertThat(list.get(0), is(saveA));
    }

}
