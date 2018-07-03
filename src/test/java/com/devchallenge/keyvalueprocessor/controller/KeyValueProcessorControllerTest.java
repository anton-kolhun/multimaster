package com.devchallenge.keyvalueprocessor.controller;


import com.devchallenge.keyvalueprocessor.MultiMasterReplicationApp;
import com.devchallenge.keyvalueprocessor.config.MockingRestTemplateConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = {MultiMasterReplicationApp.class, MockingRestTemplateConfig.class})
public class KeyValueProcessorControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    TestRestTemplate testRestTemplate;

    @Autowired
    RestTemplate restTemplate;

    @Test
    public void createOrUpdate() {
        //setup
        String url = "http://localhost:" + port + "/data";
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", MediaType.APPLICATION_JSON_VALUE);
        String payload = "{\"key\":\"value\"}";
        HttpEntity<String> request = new HttpEntity<>(payload, headers);

        //save data
        ResponseEntity<String> responseCreate = testRestTemplate.exchange(url, HttpMethod.POST, request, String.class);

        //verify
        //sync with other nodes get called
        verify(restTemplate).exchange(
                any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class)
        );
        Assert.assertEquals(payload, responseCreate.getBody());

        //read saved data
        String responseRead = testRestTemplate.getForObject(url, String.class);
        Assert.assertEquals(payload, responseRead);

        //remove data
        testRestTemplate.delete(url + "/key");

        //read just removed data data
        String responseReadAfterDelete = testRestTemplate.getForObject(url, String.class);
        Assert.assertNotEquals(payload, responseReadAfterDelete);
    }

}