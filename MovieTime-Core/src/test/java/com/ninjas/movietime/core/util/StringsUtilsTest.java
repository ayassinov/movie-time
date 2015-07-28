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

package com.ninjas.movietime.core.util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author ayassinov on 17/07/14
 */
public class StringsUtilsTest {

    @Test
    public void encodeTest() {
        final String inputString = StringUtils.encode("UGC Ciné Cité");
        Assert.assertThat(inputString, CoreMatchers.is("UGC+Cin%C3%A9+Cit%C3%A9"));
    }

    @Test
    public void trimAndLowerCase() {
        final String resUpper = StringUtils.trimAndTransform("  HEllo My MAAAN  ", StringUtils.TextTransformation.TO_UPPER);
        final String resLower = StringUtils.trimAndTransform("  HEllo My MAAAN  ", StringUtils.TextTransformation.TO_LOWER);
        Assert.assertThat(resUpper, CoreMatchers.is("HELLO MY MAAAN"));
        Assert.assertThat(resLower, CoreMatchers.is("hello my maaan"));
    }
}
