package io.idwangmo.testing.model.config;

import lombok.Data;


@Data
public class TestConfig {

    private String environment;

    private CreamsConfig creams;

    private JiraConfig jira;

    private RedisConfig redis;
}
