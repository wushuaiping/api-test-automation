package io.idwangmo.testing.model.jira;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.annotation.Generated;

@Data
@Generated("com.robohorse.robopojogenerator")
public class Step {

    @JsonProperty("rendered")
    private String rendered;

    @JsonProperty("raw")
    private String raw;
}
