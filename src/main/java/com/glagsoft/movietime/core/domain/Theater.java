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

package com.glagsoft.movietime.core.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

/**
 * @author ayassinov on 15/07/14
 */
@Getter
@Setter
@EqualsAndHashCode(of = "id")
public class Theater {

    @Id
    private String id;

    private String name;

    private Location location;

    public Theater() {
    }

    public Theater(String id, String name, Location location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }

    public Theater(String name, Location location) {
        this.name = name;
        this.location = location;
    }
}
