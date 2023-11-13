package com.studentsystemapp.model.binding;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class StudentRegisterBindingModel {

    @Size(min = 3, max = 20, message = "Username length must be between 3 and 20 characters")
    private String username;

    @Size(min = 3, max = 20, message = "Password length must be between 3 and 20 characters")
    private String password;

    @Size(min = 3, max = 20)
    private String confirmPassword;

    @Size(min = 2, max = 30, message = "First name length must be between 2 and 30 characters")
    private String firstName;

    @Size(min = 2, max = 30, message = "Last name length must be between 2 and 30 characters")
    private String lastName;

    public String getFirstName() {
        return firstName;
    }

    public StudentRegisterBindingModel setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public StudentRegisterBindingModel setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    @Email
    @NotBlank(message = "Email cannot be empty")
    private String email;

    public String getUsername() {
        return username;
    }

    public StudentRegisterBindingModel setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public StudentRegisterBindingModel setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public StudentRegisterBindingModel setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public StudentRegisterBindingModel setEmail(String email) {
        this.email = email;
        return this;
    }

}
