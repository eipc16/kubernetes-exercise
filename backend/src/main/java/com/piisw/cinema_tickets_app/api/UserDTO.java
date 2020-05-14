package com.piisw.cinema_tickets_app.api;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.ObjectState;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.Id;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Builder
public class UserDTO {

    @Id
    private Long id;

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    @Size(max = 15)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @NaturalId
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    private String role;

    private ObjectState objectState;

}

