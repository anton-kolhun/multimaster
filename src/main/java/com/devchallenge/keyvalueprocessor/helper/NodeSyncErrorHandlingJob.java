package com.devchallenge.keyvalueprocessor.helper;

import com.devchallenge.keyvalueprocessor.service.NodeSyncFailureService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class NodeSyncErrorHandlingJob implements Job {

    @Autowired
    private NodeSyncFailureService nodeSyncFailureService;


    @Override
    public void execute(JobExecutionContext context) {
        nodeSyncFailureService.handleFailures();
    }
}
