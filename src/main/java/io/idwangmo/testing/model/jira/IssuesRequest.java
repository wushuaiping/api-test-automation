package io.idwangmo.testing.model.jira;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Ts
 * 2019-03-15
 * 17:42
 */
@Getter
@Setter
public class IssuesRequest {

    private Integer projectId;
    private Integer issuesTypeId;
    private String summary;
    private String description;
    private Integer components;
    private String scenarioType;
    private String scenario;
}
