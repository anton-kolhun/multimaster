package com.devchallenge.keyvalueprocessor.service;

import com.devchallenge.keyvalueprocessor.dal.entity.KeyValueEntity;
import com.devchallenge.keyvalueprocessor.dal.repository.KeyValueRepository;
import com.devchallenge.keyvalueprocessor.exception.ValidationException;
import com.devchallenge.keyvalueprocessor.service.dto.KeyValueDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Calendar;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KeyValueProcessorService {

    @Autowired
    private KeyValueRepository keyValueRepository;

    @Transactional(readOnly = true)
    public List<KeyValueDto> getAllData() {
        List<KeyValueEntity> keyValueEntities = keyValueRepository.findAll();
        List<KeyValueDto> dtos = keyValueEntities.stream()
                .map(KeyValueProcessorService::toDto)
                .collect(Collectors.toList());
        return dtos;
    }

    @Transactional(readOnly = true)
    public KeyValueDto getDataByKey(String key) {
        KeyValueEntity keyValueEntity = keyValueRepository.findByKey(key);

        return toDto(keyValueEntity);
    }

    @Transactional
    public KeyValueDto createOrUpdate(KeyValueDto keyValueDto) {
        if (StringUtils.isEmpty(keyValueDto.getKey())) {
            throw new ValidationException("name filed cannot be empty");
        }
        if (keyValueDto.getModifiedAt() == null) {
            keyValueDto.setModifiedAt(Calendar.getInstance().getTime());
        }
        KeyValueEntity existing = keyValueRepository.findByKey(keyValueDto.getKey());
        if (existing != null && existing.getModifiedAt().compareTo(keyValueDto.getModifiedAt()) > 0) {
            return toDto(existing);
        }

        KeyValueEntity toUpdate = toEntity(keyValueDto);
        KeyValueEntity updated = keyValueRepository.save(toUpdate);
        return toDto(updated);
    }


    @Transactional
    public void remove(String key) {
        KeyValueEntity keyValueEntity = new KeyValueEntity();
        keyValueEntity.setKey(key);
        keyValueRepository.delete(keyValueEntity);

    }

    public static KeyValueDto toDto(KeyValueEntity keyValueEntity) {
        return new KeyValueDto(keyValueEntity.getKey(), keyValueEntity.getValue(), keyValueEntity.getModifiedAt());
    }

    public static KeyValueEntity toEntity(KeyValueDto keyValueDto) {
        return new KeyValueEntity(keyValueDto.getKey(), keyValueDto.getValue(), keyValueDto.getModifiedAt());
    }

}
