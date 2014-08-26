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

import com.ninjas.movietime.core.domain.Theater;
import com.ninjas.movietime.core.domain.TheaterChain;
import com.ninjas.movietime.data.TheaterRepository;
import com.ninjas.movietime.integration.allocine.TheaterAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ayassinov on 16/07/14
 */
@Service
public class TheaterService {

    private final TheaterAPI theaterAPI;

    private final TheaterRepository theaterRepository;


    @Autowired
    public TheaterService(TheaterAPI theaterAPI,  TheaterRepository theaterRepository) {
        this.theaterAPI = theaterAPI;

        this.theaterRepository = theaterRepository;
    }

    public List<Theater> update() {
        // list all theaters in paris.
        final int parisZip = 75000;
        final List<Theater> theaters =null; //this.theaterAPI.listAllByCityZip(parisZip);

        // ignore duplicate theater chain
        final Map<String, TheaterChain> theaterChains = new HashMap<>();
        for (Theater theater : theaters) {
            if(!theaterChains.containsKey(theater.getTheaterChain().getId())){
                theaterChains.put(theater.getTheaterChain().getId(), theater.getTheaterChain());
            }
        }

        //same theater chains
       // this.theaterChainRepository.save(theaterChains.values());

        //save theaters
        this.theaterRepository.save(theaters);

        return theaters;
    }


    public List<TheaterChain> listAllWithTheaters(){
        //list theaters grouped by theater chain.
        final List<TheaterChain> theaterChains = null;//theaterChainRepository.findAll();
        for (TheaterChain theaterChain : theaterChains) {
            theaterChain.setTheaters(theaterRepository.findByTheaterChain(theaterChain));
        }

        return theaterChains;
    }


}
