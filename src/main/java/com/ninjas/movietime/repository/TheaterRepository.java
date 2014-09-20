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

import com.google.common.base.Preconditions;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

/**
 * @author ayassinov on 16/07/14
 */
@Repository
public class TheaterRepository extends BaseRepository {


    @Autowired
    public TheaterRepository(MongoTemplate mongoTemplate) {
        super(mongoTemplate);
    }


    public Page<Theater> listByTheaterChain(Collection<TheaterChain> theaterChains, int page, int size) {
        Preconditions.checkArgument(theaterChains.size() > 0, "Theater Chain list cannot be empty");
        final Pageable pageable = new PageRequest(page, size, new Sort(Sort.Direction.ASC, "name"));
        final Query query = Query.query(Criteria.where("theaterChain").in(theaterChains));
        return findPaged(pageable, query, Theater.class);
    }


}
