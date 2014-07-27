package com.glagsoft.movietime.conf;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.NTCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.DefaultConnectionKeepAliveStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.ProxyAuthenticationStrategy;
import org.springframework.context.annotation.Scope;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Collections;

/**
 * @author ayassinov on 28/07/2014.
 */
@Scope
@Component
public class HttpClientFactory {

    public static final HttpClientFactory INSTANCE = new HttpClientFactory();

    private HttpClientFactory() {
    }

    private final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();

    public HttpComponentsClientHttpRequestFactory getRequestFactory(final HttpClientConfig config) {
        final HttpClient httpClient = getHttpClient(config);
        factory.setHttpClient(httpClient);
        factory.setReadTimeout(config.getReadTimeOut());
        factory.setConnectTimeout(config.getConnectTimeout());
        return factory;
    }

    private HttpClient getHttpClient(HttpClientConfig config) {
        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();

        //user agent
        clientBuilder.setUserAgent(config.getUserAgent());

        if (config.getProxy() != null) {
            setProxy(clientBuilder, config.getProxy());
            //log
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
        clientBuilder.setProxy(new HttpHost("server", config.getPort()));
        clientBuilder.setDefaultCredentialsProvider(credProvider);
        clientBuilder.setProxyAuthenticationStrategy(new ProxyAuthenticationStrategy());
    }

   /* private RestTemplate setInterceptor(RestTemplate restTemplate) {
        restTemplate.setInterceptors(Collections.<ClientHttpRequestInterceptor>singletonList(new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
                HttpHeaders headers = request.getHeaders();
                headers.add("User-Agent", configuration.getClient().getUserAgent());
                return execution.execute(request, body);
            }
        }));
        return restTemplate;
    }*/

}
