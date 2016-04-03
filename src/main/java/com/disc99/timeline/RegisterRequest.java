package com.disc99.timeline;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @Size(min = 4)
    private String username;
    @Size(min = 8)
    private String password;
    @Size(min = 8)
    private String passwordConfirmation;

    @AssertTrue
    boolean isPassword() {
        return password.equals(passwordConfirmation);
    }
}
