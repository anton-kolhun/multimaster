package com.devchallenge.keyvalueprocessor.dal.repository;

import com.devchallenge.keyvalueprocessor.dal.entity.KeyValueEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KeyValueRepository extends JpaRepository<KeyValueEntity, String> {

    KeyValueEntity findByKey(String key);

}
