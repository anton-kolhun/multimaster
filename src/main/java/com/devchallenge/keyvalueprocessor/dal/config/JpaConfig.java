package com.devchallenge.keyvalueprocessor.dal.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EntityScan("com.devchallenge.keyvalueprocessor.dal.entity")
@EnableJpaRepositories(value = "com.devchallenge.keyvalueprocessor.dal.repository")
public class JpaConfig {

}
