package com.piisw.cinema_tickets_app.api;

import lombok.Data;

@Data
public class TokenDTO {

    private String accessToken;
    private String tokenType = "Barer";

    public TokenDTO(String accessToken) {
        this.accessToken = accessToken;
    }

}
