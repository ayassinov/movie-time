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

package com.glagsoft.movietime.core.util;

import com.google.common.base.Preconditions;
import lombok.SneakyThrows;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author ayassinov on 17/07/14
 */
public abstract class StringUtils extends org.springframework.util.StringUtils {

    private static final Logger LOG = LoggerFactory.getLogger(StringUtils.class);

    @SneakyThrows(UnsupportedEncodingException.class)
    public static String encode(final String toEncode) {
        Preconditions.checkNotNull(toEncode);
        return URLEncoder.encode(toEncode, "UTF-8");
    }

    public static String transform(final String text, TextTransformation textTransformation) {
        if (textTransformation.equals(TextTransformation.TO_UPPER))
            return text.trim().toUpperCase();

        if (textTransformation.equals(TextTransformation.TO_LOWER))
            return text.trim().toLowerCase();

        return text;
    }

    public static String trimAndTransform(final String text, TextTransformation textTransformation) {
        return transform(text.trim(), textTransformation);
    }

    public enum TextTransformation {
        NONE, TO_UPPER, TO_LOWER
    }
}
