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

package com.ninjas.movietime.conf.vendor.jackson;

import com.google.common.base.Optional;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;

/**
 * Support for Guava Optional Type
 *
 * @author ayassinov on 11/07/14
 */
public class MovieTimeJsonMapper extends MappingJackson2HttpMessageConverter {

    public MovieTimeJsonMapper() {
        super();
    }

    @Override
    protected Object readInternal(Class<?> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        final Object result = super.readInternal(clazz, inputMessage);
        if (clazz.equals(Optional.class)) {
            return Optional.fromNullable(result);
        }
        return result;
    }

    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        if (object instanceof Optional) {
            object = ((Optional) object).orNull();
        }
        super.writeInternal(object, outputMessage);
    }

}
