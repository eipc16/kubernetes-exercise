package com.piisw.cinema_tickets_app.domain.user.entity;

import com.piisw.cinema_tickets_app.domain.auditedobject.entity.AuditedObject;
import com.piisw.cinema_tickets_app.infrastructure.security.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "User")
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder(toBuilder = true)
public class User extends AuditedObject {

    @NotBlank
    @Size(max = 40)
    private String name;

    @NotBlank
    @Size(max = 40)
    private String surname;

    @NotBlank
    @Size(max = 40)
    private String username;

    @NotBlank
    @Size(max = 100)
    private String password;

    @Column(unique=true)
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (!(obj instanceof User)) {
            return false;
        }

        User otherUser = (User) obj;
        return Objects.equals(otherUser.getId(), getId());
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

}
