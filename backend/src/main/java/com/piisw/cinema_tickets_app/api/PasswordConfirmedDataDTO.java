package com.piisw.cinema_tickets_app.api;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PasswordConfirmedDataDTO {

    private String password;
    private String value;

}
