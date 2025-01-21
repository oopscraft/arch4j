package org.oopscraft.arch4j.core.common.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.HttpRequestRetryStrategy;
import org.apache.hc.client5.http.config.RequestConfig;
//import org.apache.http.HttpHost;
//import org.apache.http.client.HttpRequestRetryHandler;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.config.SocketConfig;
//import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClientBuilder;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.client.StandardHttpRequestRetryHandler;
//import org.apache.http.ssl.SSLContextBuilder;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.DefaultClientTlsStrategy;

import org.apache.hc.client5.http.ssl.TlsSocketStrategy;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.ssl.SSLContextBuilder;
import org.apache.hc.core5.util.Timeout;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;

@Slf4j
public class RestTemplateBuilder {

    private boolean insecure = true;

    private String proxyHost;

    private int proxyPort;

    private String[] httpsProtocols;

    private int connectTimeout = 5_000;

    private int readTimeout = 30_000;

    private HttpRequestRetryStrategy httpRequestRetryStrategy;

    public static RestTemplateBuilder create() {
        return new RestTemplateBuilder();
    }

    public RestTemplateBuilder insecure(boolean insecure) {
        this.insecure = insecure;
        return this;
    }

    public RestTemplateBuilder proxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        return this;
    }

    public RestTemplateBuilder httpsProtocols(String[] httpsProtocols) {
        this.httpsProtocols = httpsProtocols;
        return this;
    }

    public RestTemplateBuilder connectTimeout(int connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    public RestTemplateBuilder httpRequestRetryStrategy(HttpRequestRetryStrategy httpRequestRetryStrategy) {
        this.httpRequestRetryStrategy = httpRequestRetryStrategy;
        return this;
    }

    public RestTemplate build() {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        // ssl context
        SSLContext sslContext;
        try {
            if (insecure) {
                log.debug("Insecure mode enabled. SSL certificate validation will be ignored.");
                sslContext = new SSLContextBuilder().loadTrustMaterial(null, (chain, authType) -> true).build();
            } else {
                sslContext = SSLContext.getInstance("TLS");
                sslContext.init(null, null, new java.security.SecureRandom());
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new RuntimeException("Failed to configure SSLContext", e);
        }

        // ssl socket factory
//        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(
//            sslContext,
//            httpsProtocols != null ? httpsProtocols : sslContext.getSupportedSSLParameters().getProtocols(),
//            null,
//            new DefaultHostnameVerifier()
//        );

        TlsSocketStrategy tlsSocketStrategy = new DefaultClientTlsStrategy(sslContext);

        // connection manager
        PoolingHttpClientConnectionManager connectionManager = PoolingHttpClientConnectionManagerBuilder.create()
                .setTlsSocketStrategy(tlsSocketStrategy)
//                .setSSLSocketFactory(connectionSocketFactory)
                .build();
//        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
//        connectionManager.setSSLSocketFactory(connectionSocketFactory);



        // applies ssl socket factory to http client
        httpClientBuilder.setConnectionManager(connectionManager);
//        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);

        // proxy
        if (proxyHost != null && !proxyHost.isEmpty()) {
            HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
            httpClientBuilder.setProxy(httpHost);
        }

        // timeout settings
        RequestConfig requestConfig = RequestConfig.custom()
            .setConnectionRequestTimeout(Timeout.ofMilliseconds(connectTimeout))
            .setResponseTimeout(Timeout.ofMilliseconds(readTimeout))
            .build();
        httpClientBuilder.setDefaultRequestConfig(requestConfig);

        // retry handler
        if (httpRequestRetryStrategy != null) {
            httpClientBuilder.setRetryStrategy(httpRequestRetryStrategy);
        }

        // build
        CloseableHttpClient httpClient = httpClientBuilder.build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        return new RestTemplate(clientHttpRequestFactory);
    }

}
