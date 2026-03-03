package com.anish.teams.service.impl;

import com.anish.teams.config.WebClientConfig;
import com.anish.teams.config.properties.TeamsChannelProperties;
import com.anish.teams.dto.NotificationRequestDto;
import com.anish.teams.service.TeamsService;
import com.anish.teams.util.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ArrayNode;
import tools.jackson.databind.node.ObjectNode;

import java.net.URI;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class TeamsServiceImpl implements TeamsService {

    private final TeamsChannelProperties teamsChannelProperties;

    private final ObjectMapper objectMapper;

    private final WebClient webClient;

    @Override
    @Async("asyncExecutor")
    public void sendChannelNotification(NotificationRequestDto request) {
        try {
            log.info("Sending channel notification with payload: {}", request);
            ObjectNode payload = buildTeamsRequest(request);
            sendMessage(payload);
            log.info("Channel notification sent");
        } catch(WebClientResponseException e) {
            log.error("Error sending channel notification: {}", e.getMessage(), e);
        }
    }

    private void sendMessage(ObjectNode payload) {
        URI uri = URI.create(teamsChannelProperties.webhookUrl());
        webClient.post()
                .uri(uri)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .doOnNext(body -> log.info("Response: {}", body))
                .block();
    }

    private ObjectNode buildTeamsRequest(NotificationRequestDto request) {
        ObjectNode root = objectMapper.createObjectNode();
        root.put(Constants.CommonConstants.TYPE, Constants.CommonConstants.MESSAGE);

        ArrayNode attachments = root.putArray(Constants.CommonConstants.ATTACHMENTS);
        ObjectNode attachment = attachments.addObject();
        attachment.put(Constants.CommonConstants.CONTENT_TYPE,
                Constants.TeamsConstants.CONTENT_TYPE_VALUE);

        ObjectNode content = attachment.putObject(Constants.CommonConstants.CONTENT);
        content.put(Constants.CommonConstants.TYPE, Constants.TeamsConstants.ADAPTIVE_CARD);

        // Body
        ArrayNode body = content.putArray(Constants.CommonConstants.BODY);
        body.add(createTextBlock(
                request.title(),
                Constants.TeamsConstants.EXTRA_LARGE)
        );
        body.add(createTextBlock(
                request.subtitle(),
                Constants.TeamsConstants.LARGE)
        );
        body.add(createTextBlock(
                request.content(),
                Constants.TeamsConstants.MEDIUM)
        );

        // Actions
        ArrayNode actions = content.putArray(Constants.CommonConstants.ACTIONS);
        ObjectNode action = actions.addObject();
        action.put(Constants.CommonConstants.TYPE, Constants.TeamsConstants.ACTION_TYPE_VALUE);
        action.put(Constants.CommonConstants.TITLE, request.buttonTitle());
        action.put(Constants.CommonConstants.URL, request.url());
        return root;
    }

    private ObjectNode createTextBlock(String text, String size) {
        ObjectNode textBlock = objectMapper.createObjectNode();
        textBlock.put(Constants.CommonConstants.TYPE, Constants.TeamsConstants.TEXT_BLOCK);
        textBlock.put(Constants.CommonConstants.TEXT, text);
        textBlock.put(Constants.CommonConstants.SIZE, size);
        textBlock.put(Constants.CommonConstants.WEIGHT, Constants.TeamsConstants.BOLDER);
        textBlock.put(Constants.CommonConstants.WRAP, Boolean.TRUE);
        textBlock.put(Constants.CommonConstants.STYLE, Constants.CommonConstants.HEADING);
        return textBlock;
    }
}
