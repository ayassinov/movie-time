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

package com.ninjas.movietime.service;

import com.ninjas.movietime.data.TheaterRepository;
import com.ninjas.movietime.integration.allocine.MovieAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author ayassinov on 16/07/14
 */
@Service
public class MovieService {

    private final MovieAPI movieAPI;

    private final TheaterRepository theaterRepository;

    @Autowired
    public MovieService(MovieAPI movieAPI, TheaterRepository theaterRepository) {
        this.movieAPI = movieAPI;
        this.theaterRepository = theaterRepository;
    }
}
