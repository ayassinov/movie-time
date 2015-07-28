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

package com.ninjas.movietime.core.domain.theater;

import com.google.common.collect.ImmutableList;
import com.ninjas.movietime.core.util.DateUtils;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

/**
 * @author ayassinov on 25/07/14
 */
@Getter
@ToString
@TypeAlias("theaterChains")
@Document(collection = TheaterChain.DOCUMENT_NAME)
@EqualsAndHashCode(of = "id")
public class TheaterChain {

    public final static String DOCUMENT_NAME = "theaterChain";

    private static final List<String> OFFICIAL_THEATER_CHAIN = ImmutableList
            .of("81001") //UGC only for now
            .asList();

    @Id
    private String id;

    @Indexed
    private String name;

    private boolean isTracked;

    private Date lastUpdate;

    @Setter
    @Transient
    private List<Theater> theaters;

    public TheaterChain() {
    }

    public TheaterChain(String id) {
        this.id = id;
    }

    public TheaterChain(String id, String name) {
        this.id = id;
        this.name = name;
        this.lastUpdate = DateUtils.nowServerDate();
        this.isTracked = isTracked();
    }

    public boolean isTracked() {
        return OFFICIAL_THEATER_CHAIN.contains(this.id);
    }
}
