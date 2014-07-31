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

package com.ninjas.movietime.integration.allocine.request;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ayassinov on 15/07/14
 */
public class Builder {

    private List<Parameter> parameters = new ArrayList<>();

    public static Builder create() {
        return new Builder();
    }

    public static Builder create(Parameter parameter) {
        final Builder builder = create();
        builder.parameters.add(parameter);
        return builder;
    }

    public static Builder create(String name, String value) {
        return create(new Parameter(name, value));
    }

    public Builder add(String name, String value) {
        parameters.add(new Parameter(name, value));
        return this;
    }

    public Builder add(Parameter parameter) {
        parameters.add(parameter);
        return this;
    }

    public Builder addAll(final List<Parameter> inParameters) {
        for (final Parameter inParameter : inParameters) {
            parameters.add(inParameter);
        }
        return this;
    }

    public List<Parameter> build() {
        return parameters;
    }
}
