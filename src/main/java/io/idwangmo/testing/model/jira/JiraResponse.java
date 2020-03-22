package io.idwangmo.testing.model.jira;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;
import java.util.List;

@lombok.Data
@Generated("com.robohorse.robopojogenerator")
public class JiraResponse {

    @JsonProperty("result")
    private Result result;

    @JsonProperty("attachments")
    private List<Object> attachments;

    @JsonProperty("data")
    private Data data;

    @JsonProperty("index")
    private int index;

    @JsonProperty("step")
    private Step step;

    @JsonProperty("id")
    private int id;
}
