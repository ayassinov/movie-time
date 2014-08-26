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

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.Date;

/**
 * @author ayassinov
 */
@Getter
@ToString
@EqualsAndHashCode(of = {"dateStart", "dateEnd"})
public class ShutDownStatus {

    private final Date dateStart;

    private final Date dateEnd;

    public ShutDownStatus(Date dateStart, Date dateEnd) {
        this.dateStart = dateStart;
        this.dateEnd = dateEnd;
    }
}
