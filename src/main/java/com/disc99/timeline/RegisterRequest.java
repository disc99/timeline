package com.disc99.timeline;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Size;

@Data
public class RegisterRequest {
    @UserName
    private String username;
    @Password
    private String password;
    @Password
    private String passwordConfirmation;

    @AssertTrue
    boolean isPassword() {
        return password.equals(passwordConfirmation);
    }
}
