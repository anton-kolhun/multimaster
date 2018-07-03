package com.devchallenge.keyvalueprocessor.service;

import com.devchallenge.keyvalueprocessor.service.dto.KeyValueDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Service
@Slf4j
public class MultimasterDataService {

    public static final String DATA_CREATION_ROOT_CONTEXT = "/data";
    public static final String DATA_SYNC_ENDPOINT = "/node-sync";


    @Autowired
    private KeyValueProcessorService keyValueProcessorService;

    @Autowired
    private NodeSyncService nodeSyncService;

    @Value("#{'${network.node.list}'.split(',')}")
    private List<String> nodeAddresses;


    public KeyValueDto createOrUpdate(KeyValueDto keyValueDto) {
        KeyValueDto updatedResult = keyValueProcessorService.createOrUpdate(keyValueDto);
        for (String nodeAddress : nodeAddresses) {
            String url = UriComponentsBuilder.fromHttpUrl("http://" + nodeAddress +
                    DATA_CREATION_ROOT_CONTEXT + DATA_SYNC_ENDPOINT)
                    .build()
                    .toUriString();
            nodeSyncService.updateNode(updatedResult, url);
        }
        return updatedResult;
    }


}



