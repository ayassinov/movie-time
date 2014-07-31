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

import com.codahale.metrics.MetricRegistry;
import com.ninjas.movietime.core.metrics.HttpClientMetricNameStrategies;
import com.ninjas.movietime.core.metrics.InstrumentedHttpClientConnectionManager;
import com.ninjas.movietime.core.metrics.InstrumentedHttpRequestExecutor;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;

/**
 * @author ayassinov on 28/07/2014.
 */
@Scope
@Component
public class HttpClientFactory {
    private static final Logger LOG = LoggerFactory.getLogger(HttpClientFactory.class);
    public static final HttpClientFactory INSTANCE = new HttpClientFactory();

    private HttpClientFactory() {
    }

    private final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

    public HttpComponentsClientHttpRequestFactory getRequestFactory(final MetricRegistry registry,
                                                                    final HttpClientConfig config) {
        final HttpClient httpClient = getHttpClient(registry, config);
        factory.setHttpClient(httpClient);
        factory.setReadTimeout(config.getReadTimeOut());
        factory.setConnectTimeout(config.getConnectTimeout());
        return factory;
    }

    private HttpClient getHttpClient(final MetricRegistry registry, final HttpClientConfig config) {
        //activate metrics
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        //instrumentation
        if (config.isActivateMetrics()) {
            clientBuilder.setRequestExecutor(
                    new InstrumentedHttpRequestExecutor(registry, HttpClientMetricNameStrategies.HOST_AND_METHOD));
            clientBuilder.setConnectionManager(
                    new InstrumentedHttpClientConnectionManager(registry));
            LOG.info("HTTP Client created with instrumentation");
        }

        //user agent
        clientBuilder.setUserAgent(config.getUserAgent());

        //proxy configuration
        if (config.getProxy() != null) {
            setProxy(clientBuilder, config.getProxy());
            LOG.info("HTTP Client created with Proxy configuration");
        }

        return clientBuilder.build();
    }

    private void setProxy(HttpClientBuilder clientBuilder, ProxyConfig config) {
        CredentialsProvider credProvider = new BasicCredentialsProvider();
        credProvider.setCredentials(
                new AuthScope(config.getServer(), config.getPort(), AuthScope.ANY_REALM, "ntlm"),
                new NTCredentials(config.getUserName(), config.getPassword(), null, null)
        );
        clientBuilder.useSystemProperties();
        clientBuilder.setProxy(new HttpHost(config.getServer(), config.getPort()));
        clientBuilder.setDefaultCredentialsProvider(credProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
    }


}
