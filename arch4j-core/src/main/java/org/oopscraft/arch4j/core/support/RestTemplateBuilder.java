package org.oopscraft.arch4j.core.support;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.security.cert.CertificateException;

@Slf4j
public class RestTemplateBuilder {


    private boolean insecure = true;

    private String proxyHost;

    private int proxyPort;

    private int connectTimeout = 3000;

    private int readTimeout = 1000*10;

    /**
     * create
     * @return
     */
    public static RestTemplateBuilder create() {
        return new RestTemplateBuilder();
    }

    /**
     * insecure
     * @param insecure
     * @return
     */
    public RestTemplateBuilder insecure(boolean insecure) {
        this.insecure = insecure;
        return this;
    }

    /**
     * proxy
     * @param proxyHost
     * @param proxyPort
     * @return
     */
    public RestTemplateBuilder proxy(String proxyHost, int proxyPort) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        return this;
    }

    /**
     * connectTimeout
     * @param connectTimeout
     * @return
     */
    public RestTemplateBuilder connectTimeout(int connectTimeout){
        this.connectTimeout = connectTimeout;
        return this;
    }

    /**
     * readTimeout
     * @param readTimeout
     * @return
     */
    public RestTemplateBuilder readTimeout(int readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }

    /**
     * build
     * @return
     */
    public RestTemplate build() {

        HttpClientBuilder httpClientBuilder = HttpClients.custom();

        // insecure
        if(insecure) {
            try {
                SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustStrategy() {
                    @Override
                    public boolean isTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        return true;
                    }
                }).build();
                SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext);
                httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

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
