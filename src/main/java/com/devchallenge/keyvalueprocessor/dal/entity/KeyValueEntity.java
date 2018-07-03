package com.devchallenge.keyvalueprocessor.dal.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "KEY_VALUE")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueEntity {

    @Id
    @Column(name = "KEY")
    private String key;

    @Column(name = "VALUE")
    private String value;

    @Column(name = "MODIFIED_AT")
    private Date modifiedAt;


}
