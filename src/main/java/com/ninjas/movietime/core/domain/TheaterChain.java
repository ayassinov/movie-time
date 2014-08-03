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

package com.ninjas.movietime.core.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 25/07/14
 */
@Getter
@ToString
//@JsonIgnoreProperties({"theaters"})
@TypeAlias("theaterChains")
@Document(collection = "theaterChain")
@EqualsAndHashCode(of = "code")
public class TheaterChain {

    @Id
    private final String code;

    @Indexed
    private final String name;

    @Setter
    private List<Theater> theaters = new ArrayList<>();

    @JsonCreator
    public TheaterChain(@JsonProperty("code") String code,
                        @JsonProperty("$") String name) {
        this.code = code;
        this.name = name;
    }
}
