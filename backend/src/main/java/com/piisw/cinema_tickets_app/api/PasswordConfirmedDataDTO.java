package com.piisw.cinema_tickets_app.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordConfirmedDataDTO {

    private String password;
    private String value;

}
