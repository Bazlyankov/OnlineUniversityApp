package com.studentsystemapp.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRegisterBindingModel {


    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters")
    private String username;

    @Size(min = 2, max = 20, message = "First name length must be between 3 and 20 characters")
    private String firstName;

    @Size(min = 2, max = 20, message = "Last name length must be between 3 and 20 characters")
    private String lastName;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    private String password;

    @Size(min = 3, max = 20)

    private String confirmPassword;


    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

}
