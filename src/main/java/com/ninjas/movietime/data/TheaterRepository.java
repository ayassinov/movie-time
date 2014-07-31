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

import com.ninjas.movietime.core.domain.Theater;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author ayassinov on 16/07/14
 */
@Repository
public interface TheaterRepository extends MongoRepository<Theater, String> {
    // We can use any combination of :
    // Distinct, Between, LessThan, GreaterThan, Like, AND, OR,  IgnoreCase, AllIgnoreCase,  OrderBy, Asc, Desc
    //

    /**
     * Find movieTheatre by name like ignore case and order by name asc
     *
     * @param name the name of theatre
     * @return list of theatre ordered by name
     */
    List<Theater> findByNameLikeIgnoreCaseOrderByNameAsc(String name);


    List<Theater> findByGeoLocationLongitude(String longitude);

}
