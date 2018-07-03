package com.devchallenge.keyvalueprocessor.controller;

import com.devchallenge.keyvalueprocessor.service.KeyValueProcessorService;
import com.devchallenge.keyvalueprocessor.service.MultimasterDataService;
import com.devchallenge.keyvalueprocessor.service.dto.KeyValueDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping(MultimasterDataService.DATA_CREATION_ROOT_CONTEXT)
public class KeyValueProcessorController {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KeyValueProcessorService keyValueProcessorService;

    @Autowired
    private MultimasterDataService multimasterDataService;

    @GetMapping
    public Map<String, String> getAllData() {

        List<KeyValueDto> keyValuesDtos = keyValueProcessorService.getAllData();
        Map<String, String> keyValues = convertToMap(keyValuesDtos);
        return keyValues;
    }

    private Map<String, String> convertToMap(List<KeyValueDto> keyValuesDtos) {
        return keyValuesDtos.stream()
                .collect(Collectors.toMap(KeyValueDto::getKey, KeyValueDto::getValue));
    }

    @GetMapping("{name}")
    public Map<String, String> getByName(@PathVariable("name") String key) {

        KeyValueDto keyValue = keyValueProcessorService.getDataByKey(key);
        return Collections.singletonMap(keyValue.getKey(), keyValue.getValue());
    }

    //used for node communication
    @PostMapping(MultimasterDataService.DATA_SYNC_ENDPOINT)
    public Map<String, String> createOrUpdate(@RequestBody KeyValueDto keyValueDto) {
        KeyValueDto updated = keyValueProcessorService.createOrUpdate(keyValueDto);
        return Collections.singletonMap(updated.getKey(), updated.getValue());
    }

    @PostMapping
    public Map<String, String> createOrUpdate(@RequestBody Map<String, String> keyValue) {
        KeyValueDto keyValueDto = convertMapToDto(keyValue);
        KeyValueDto updated = multimasterDataService.createOrUpdate(keyValueDto);
        return Collections.singletonMap(updated.getKey(), updated.getValue());
    }

    private KeyValueDto convertMapToDto(@RequestBody Map<String, String> keyValue) {
        Map.Entry<String, String> entry = keyValue.entrySet().iterator().next();
        KeyValueDto keyValueDto = new KeyValueDto();
        keyValueDto.setKey(entry.getKey());
        keyValueDto.setValue(entry.getValue());
        return keyValueDto;
    }


    @DeleteMapping("{name}")
    public void remove(@PathVariable("name") String key) {
        keyValueProcessorService.remove(key);
    }

    @GetMapping(value = "dump/{fileName}")
    public ResponseEntity dumpData(@PathVariable String fileName) throws Exception {
        List<KeyValueDto> keyValuesDtos = keyValueProcessorService.getAllData();

        Map<String, String> keyValues = convertToMap(keyValuesDtos);

        String jsonResult = objectMapper.writeValueAsString(keyValues);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + fileName)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(jsonResult);
    }


}
