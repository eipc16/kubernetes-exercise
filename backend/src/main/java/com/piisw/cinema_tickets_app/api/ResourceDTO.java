package com.piisw.cinema_tickets_app.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.net.URI;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceDTO {

    private Long id;
    private String identifier;
    private URI uri;

}
