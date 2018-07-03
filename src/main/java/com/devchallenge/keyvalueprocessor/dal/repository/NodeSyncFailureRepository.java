package com.devchallenge.keyvalueprocessor.dal.repository;

import com.devchallenge.keyvalueprocessor.dal.entity.NodeSyncFailureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NodeSyncFailureRepository extends JpaRepository<NodeSyncFailureEntity, Long> {
}
