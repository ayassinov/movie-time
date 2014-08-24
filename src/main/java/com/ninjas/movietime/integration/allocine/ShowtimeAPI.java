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

import org.springframework.stereotype.Service;

/**
 * @author ayassinov on 30/07/2014.
 */
@Service
public class ShowtimeAPI {

//    protected ShowtimeAPI() {
//        super("showtimelist");
//    }
//
//    public List<Showtime> findByTheaters(List<String> theaterCodes, Optional<String> movieCode, Optional<Date> date) {
//        final String theaters = Joiner.on(",").join(theaterCodes);
//        final RequestBuilder.Parameter.Builder paramBuilder = RequestBuilder.Parameter.Builder.create();
//        paramBuilder.add("theaters", theaters);
//
//        if (movieCode.isPresent()) {
//            paramBuilder.add("movie", movieCode.get());
//        }
//
//        if (date.isPresent()) {
//            //todo move date format to an utility class
//            paramBuilder.add("date", new SimpleDateFormat("YYYY-MM-DD").format(date.get()));
//        }
//
//        return get(paramBuilder.build()).getFeedResponse().getShowtime();
//    }
}
