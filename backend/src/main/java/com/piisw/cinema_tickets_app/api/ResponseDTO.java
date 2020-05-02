package com.piisw.cinema_tickets_app.api;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDTO {

    private boolean success;
    private String message;

}
