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

package com.ninjas.movietime.service;

import com.codahale.metrics.Timer;
import com.google.common.base.Optional;
import com.ninjas.movietime.core.domain.theater.Theater;
import com.ninjas.movietime.core.domain.theater.TheaterChain;
import com.ninjas.movietime.core.util.MetricManager;
import com.ninjas.movietime.repository.TheaterChainRepository;
import com.ninjas.movietime.repository.TheaterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.Collection;

/**
 * @author ayassinov on 16/07/14
 */
@Service
public class TheaterService {

    private final String className = this.getClass().getCanonicalName();

    private final TheaterRepository theaterRepository;

    private final TheaterChainRepository theaterChainRepository;

    @Autowired
    public TheaterService(TheaterRepository theaterRepository, TheaterChainRepository theaterChainRepository) {
        this.theaterRepository = theaterRepository;
        this.theaterChainRepository = theaterChainRepository;
    }

    public Page<Theater> listAll(int page, int size) {
        final Optional<Timer.Context> timer = MetricManager.startTimer(className, "getAppInformation");
        try {
            final Collection<TheaterChain> theaterChains = theaterChainRepository.listOfficialTheaterChainIds();
            return theaterRepository.listByTheaterChain(theaterChains, page, size);
        } finally {
            MetricManager.stopTimer(timer);
        }
    }
}
