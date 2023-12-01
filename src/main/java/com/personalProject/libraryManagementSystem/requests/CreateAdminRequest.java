package com.personalProject.libraryManagementSystem.requests;

import com.personalProject.libraryManagementSystem.modals.Admin;
import lombok.*;

import javax.validation.constraints.NotBlank;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CreateAdminRequest {
    private String name;

    private String address;
    @NotBlank
    private String contact;

    private String email;

    private String password;

    public Admin to() {
        return Admin.builder().
                name(this.name).
                email(this.email).
                contact(this.contact).
                address(this.address).
                build();
    }
}
