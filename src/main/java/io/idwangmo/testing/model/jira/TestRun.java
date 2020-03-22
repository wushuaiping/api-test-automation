package io.idwangmo.testing.model.jira;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
@Generated("com.robohorse.robopojogenerator")
public class TestRun {

    @JsonProperty("id")
    private Long id;

    @JsonProperty("testKey")
    private String testKey;

    @JsonProperty("testExecKey")
    private String testExecKey;

    @JsonProperty("status")
    private String status;

}
