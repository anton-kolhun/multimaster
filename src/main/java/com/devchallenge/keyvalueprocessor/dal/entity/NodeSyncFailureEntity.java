package com.devchallenge.keyvalueprocessor.dal.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "SYNC_FAILURE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NodeSyncFailureEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "NODE_URL")
    private String nodeUrl;

    @ManyToOne
    @JoinColumn(name = "KEY_VALUE_ID")
    private KeyValueEntity keyValueEntity;
}
