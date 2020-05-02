package com.piisw.cinema_tickets_app.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class LoginDataDTO {

    @NotNull
    @NotBlank
    private String usernameOrEmail;

    @NotNull
    @NotBlank
    private String password;

}
