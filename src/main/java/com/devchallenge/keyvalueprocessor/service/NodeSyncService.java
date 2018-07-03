package com.devchallenge.keyvalueprocessor.service;

import com.devchallenge.keyvalueprocessor.dal.entity.NodeSyncFailureEntity;
import com.devchallenge.keyvalueprocessor.dal.repository.NodeSyncFailureRepository;
import com.devchallenge.keyvalueprocessor.service.dto.KeyValueDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class NodeSyncService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NodeSyncFailureRepository nodeSyncFailureRepository;

    @Value("#{'${network.node.list}'.split(',')}")
    private List<String> nodeAddresses;


    @Async
    public void updateNode(KeyValueDto keyValueDto, String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        try {
            HttpEntity<KeyValueDto> request = new HttpEntity<>(keyValueDto, headers);
            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, request, Map.class);
        } catch (RestClientException e) {
            log.warn("error occurred while calling node " + url, e);
            saveFailure(keyValueDto, url);
        }
    }

    private void saveFailure(KeyValueDto keyValueDto, String nodeUrl) {
        NodeSyncFailureEntity nodeSyncFailureEntity = new NodeSyncFailureEntity();
        nodeSyncFailureEntity.setKeyValueEntity(KeyValueProcessorService.toEntity(keyValueDto));
        nodeSyncFailureEntity.setNodeUrl(nodeUrl);
        nodeSyncFailureRepository.save(nodeSyncFailureEntity);
    }
}

