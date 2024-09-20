package org.oopscraft.arch4j.core.common.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.util.Optional;

@Slf4j
public class RestTemplateBuilder {

    private boolean insecure = true;

    private String proxyHost;

    private int proxyPort;

    private String[] httpsProtocols;

    private int connectTimeout = 3000;

    private int readTimeout = 1000*10;

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

    public RestTemplateBuilder connectTimeout(int connectTimeout){
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
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
            throw new RuntimeException(e);
        }

        // ssl socket factory
        SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(
                sslContext,
                Optional.ofNullable(this.httpsProtocols).orElse(sslContext.getSupportedSSLParameters().getProtocols()),
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier()
        );

        // applies ssl socket factory to http client
        httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);

        // proxy
        if(StringUtils.isNotBlank(proxyHost)) {
            HttpHost httpHost = new HttpHost(proxyHost, proxyPort);
            httpClientBuilder.setProxy(httpHost);
        }

        // build
        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(readTimeout)
                .build();
        CloseableHttpClient httpClient = httpClientBuilder
                .setDefaultSocketConfig(socketConfig)
                .build();
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClient);
        clientHttpRequestFactory.setConnectTimeout(connectTimeout);
        clientHttpRequestFactory.setReadTimeout(readTimeout);
        return new RestTemplate(clientHttpRequestFactory);
    }

}
