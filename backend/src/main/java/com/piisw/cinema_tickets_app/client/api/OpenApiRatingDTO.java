package com.piisw.cinema_tickets_app.client.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OpenApiRatingDTO {

    @JsonProperty(value = "Source")
    private String source;

    @JsonProperty(value = "Value")
    private String rate;

}
