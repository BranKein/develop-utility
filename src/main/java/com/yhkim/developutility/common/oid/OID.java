package com.yhkim.developutility.common.oid;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OID {
    @JsonProperty("d")
    private String domain;

    @JsonProperty("c")
    private String comment;

    @JsonProperty("w")
    private boolean deprecated;
}
