package com.devchallenge.keyvalueprocessor.service;

import com.devchallenge.keyvalueprocessor.dal.entity.NodeSyncFailureEntity;
import com.devchallenge.keyvalueprocessor.dal.repository.NodeSyncFailureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NodeSyncFailureService {

    @Autowired
    private NodeSyncFailureRepository nodeSyncFailureRepository;

    @Autowired
    private NodeSyncService nodeSyncService;

    public void handleFailures() {
        List<NodeSyncFailureEntity> failures = nodeSyncFailureRepository.findAll();
        for (NodeSyncFailureEntity failure : failures) {
            nodeSyncFailureRepository.delete(failure);
            nodeSyncService.updateNode(KeyValueProcessorService.toDto(failure.getKeyValueEntity()),
                    failure.getNodeUrl());
        }
    }


}
