package io.idwangmo.testing.model.jira;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * 测试结果集合的模型.
 *
 * @author idwangmo
 * @since 1.0
 */
@Data
public class TestExec {
    @JsonProperty("id")
    private Long id;

    @JsonProperty("key")
    private String key;

    @JsonProperty("rank")
    private String rank;

    @JsonProperty("status")
    private String status;

}
