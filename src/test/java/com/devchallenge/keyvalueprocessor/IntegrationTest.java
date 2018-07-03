package com.devchallenge.keyvalueprocessor;


import com.spotify.docker.client.DefaultDockerClient;
import com.spotify.docker.client.exceptions.DockerException;
import com.spotify.docker.client.messages.ContainerConfig;
import com.spotify.docker.client.messages.ContainerCreation;
import com.spotify.docker.client.messages.HostConfig;
import com.spotify.docker.client.messages.PortBinding;
import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.lang.StringUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class IntegrationTest {

    @Getter(AccessLevel.NONE)
    private DefaultDockerClient docker;

    @Getter(AccessLevel.NONE)
    private String containerId;

    @Test
    public void doTest() throws Exception {
        DefaultDockerClient.Builder builder = DefaultDockerClient.builder();
        builder.uri("unix:///var/run/docker.sock");
        try {
            docker = builder.build();
            validateDockerClient(docker);
            ContainerCreation creation = createContainer();
            containerId = creation.id();
            docker.startContainer(containerId);
        } catch (DockerException | InterruptedException | IOException e) {
            destroy();
            throw new RuntimeException("Error occurred while starting docker mssql container...", e);
        }
    }

    private void validateDockerClient(DefaultDockerClient dockerClient) throws DockerException, InterruptedException {
        String ping = dockerClient.ping();
        if (!"OK".equals(ping)) {
            throw new RuntimeException("Error occurred: cannot access local docker server...");
        }
    }

    private ContainerCreation createContainer() throws DockerException, InterruptedException, IOException {
        //TODO: once https://github.com/spotify/docker-client/pull/1013 PR is merged
        // and new com.spotify:docker-client artifact is released - add authentication with ECR

        String imageName = "multimaster";
        String dockerMssqlPort = String.valueOf(8080);
        List<PortBinding> hostPorts = Collections.singletonList(PortBinding.of("0.0.0.0", 8080));
        Map<String, List<PortBinding>> portBindings = Collections.singletonMap(dockerMssqlPort, hostPorts);
        HostConfig hostConfig = HostConfig.builder().portBindings(portBindings).build();
        ContainerConfig containerConfig = ContainerConfig.builder()
                .image(imageName)
                .hostConfig(hostConfig)
                .exposedPorts(new String[]{dockerMssqlPort})
                .build();

        return docker.createContainer(containerConfig);
    }

    @After
    public void destroy() throws Exception {
        if (docker != null && StringUtils.isNotEmpty(containerId)) {
            docker.killContainer(containerId);
            docker.removeContainer(containerId);
            docker.close();
        }
    }

    @Before
    public void init() {

    }

}
