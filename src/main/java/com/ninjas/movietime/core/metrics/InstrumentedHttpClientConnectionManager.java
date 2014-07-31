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

package com.ninjas.movietime.core.metrics;

import com.codahale.metrics.Gauge;
import com.codahale.metrics.MetricRegistry;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.*;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultDnsResolver;

import java.util.concurrent.TimeUnit;

import static com.codahale.metrics.MetricRegistry.name;

/**
 * A {@link HttpClientConnectionManager} which monitors the number of open connections.
 */
public class InstrumentedHttpClientConnectionManager extends PoolingHttpClientConnectionManager {

    protected static Registry<ConnectionSocketFactory> getDefaultRegistry() {
        return RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.getSocketFactory())
                .register("https", SSLConnectionSocketFactory.getSocketFactory())
                .build();
    }

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricRegistry) {
        this(metricRegistry, getDefaultRegistry());
    }

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry) {
        this(metricsRegistry, socketFactoryRegistry, -1, TimeUnit.MILLISECONDS);
    }


    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry,
                                                   long connTTL,
                                                   TimeUnit connTTLTimeUnit) {
        this(metricsRegistry, socketFactoryRegistry, null, null, SystemDefaultDnsResolver.INSTANCE, connTTL, connTTLTimeUnit, null);
    }

    public InstrumentedHttpClientConnectionManager(MetricRegistry metricsRegistry,
                                                   Registry<ConnectionSocketFactory> socketFactoryRegistry,
                                                   HttpConnectionFactory<HttpRoute, ManagedHttpClientConnection> connFactory,
                                                   SchemePortResolver schemePortResolver,
                                                   DnsResolver dnsResolver,
                                                   long connTTL,
                                                   TimeUnit connTTLTimeUnit,
                                                   String name) {
        super(socketFactoryRegistry, connFactory, schemePortResolver, dnsResolver, connTTL, connTTLTimeUnit);
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "available-connections"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        // this acquires a lock on the connection pool; remove if contention sucks
                        return getTotalStats().getAvailable();
                    }
                });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "leased-connections"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        // this acquires a lock on the connection pool; remove if contention sucks
                        return getTotalStats().getLeased();
                    }
                });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "max-connections"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        // this acquires a lock on the connection pool; remove if contention sucks
                        return getTotalStats().getMax();
                    }
                });
        metricsRegistry.register(name(HttpClientConnectionManager.class, name, "pending-connections"),
                new Gauge<Integer>() {
                    @Override
                    public Integer getValue() {
                        // this acquires a lock on the connection pool; remove if contention sucks
                        return getTotalStats().getPending();
                    }
                });
    }
}