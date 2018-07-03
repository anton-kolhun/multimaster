package com.devchallenge.keyvalueprocessor.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class KeyValueDto {

    private String key;

    private String value;

    private Date modifiedAt;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
