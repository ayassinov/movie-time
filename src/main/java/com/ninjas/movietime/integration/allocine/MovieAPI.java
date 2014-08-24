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
 * @author ayassinov on 17/07/14
 */
@Service
public class MovieAPI  {

//
//    public MovieAPI() {
//        super("movie");
//    }
//
//    /**
//     * Get a movie information using the allo cine code.
//     *
//     * @param movieId Code of the film in allo cine.
//     * @return movie details in the json text format
//     * throws IllegalArgumentException if movieId is null or empty
//     */
//    public Optional<Movie> findById(String movieId) {
//        Preconditions.checkArgument(!Strings.isNullOrEmpty(movieId));
//        final RequestBuilder.Parameter.Builder parameterBuilder = RequestBuilder.Parameter.Builder.create("code", movieId);
//        final Movie movie = get(parameterBuilder.build()).getMovie();
//        return Optional.fromNullable(movie);
//    }
//
//    public String findByName(String name, int count) {
//        return search(name, SearchFilterEnum.MOVIE, count, String.class);
//    }
}
