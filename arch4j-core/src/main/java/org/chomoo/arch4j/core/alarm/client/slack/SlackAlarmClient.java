package org.chomoo.arch4j.core.alarm.client.slack;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.chomoo.arch4j.core.alarm.client.AlarmClient;
import org.chomoo.arch4j.core.common.support.RestTemplateBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Slf4j
@Component
public class SlackAlarmClient extends AlarmClient {

    private final String url;

    private final boolean insecure;

    public SlackAlarmClient(Properties config) {
        super(config);
        this.url = config.getProperty("url");
        this.insecure = Optional.ofNullable(config.getProperty("insecure"))
                .map(Boolean::parseBoolean)
                .orElse(false);
    }

    @Override
    public void sendMessage(String subject, String content) {
        RestTemplate restTemplate = RestTemplateBuilder.create()
                .insecure(this.insecure)
                .build();
        Map<String, Object> payload = new LinkedHashMap<>();
        List<Map<String,Object>> blocks = new ArrayList<>();
        Map<String,Object> block = new LinkedHashMap<String,Object>(){{
            put("type", "section");
            put("text", new LinkedHashMap<String,Object>(){{
                put("type", "mrkdwn");
                put("text", StringUtils.abbreviate(subject + '\n' + Optional.ofNullable(content).orElse(""), 3000));
            }});
        }};
        blocks.add(block);
        payload.put("blocks", blocks);

        RequestEntity<Map<String,Object>> requestEntity = RequestEntity
                .post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .body(payload);

        ResponseEntity<String> responseEntity = restTemplate.exchange(requestEntity, String.class);
        if (!responseEntity.getStatusCode().is2xxSuccessful() && !responseEntity.getStatusCode().is3xxRedirection()) {
            throw new RuntimeException(responseEntity.getStatusCode() + "-" + responseEntity.getBody());
        }

        log.info("== response:{}", responseEntity);
    }

}
