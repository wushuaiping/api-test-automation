package io.idwangmo.testing.model.jira;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.annotation.Generated;

@Generated("com.robohorse.robopojogenerator")
@lombok.Data
public class Data {

    @JsonProperty("rendered")
    private String rendered;

    @JsonProperty("raw")
    private String raw;
}
