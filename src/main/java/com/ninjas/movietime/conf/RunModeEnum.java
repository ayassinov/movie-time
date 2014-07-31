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

package com.ninjas.movietime.conf;

/**
 * @author ayassinov
 */
public enum RunModeEnum {

    DEV, PROD, TEST;

    public String toString() {

        switch (this) {
            case DEV:
                return "development";
            case PROD:
                return "production";
            case TEST:
                return "test";
            default:
                throw new EnumConstantNotPresentException(RunModeEnum.class, this.name());
        }
    }


}
