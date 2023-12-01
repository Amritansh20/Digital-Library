package com.personalProject.libraryManagementSystem.requests;

import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.StudentType;
import com.personalProject.libraryManagementSystem.modals.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateStudentRequest {

    private String name;

    private String address;

    private String contact;

    private String email;

    private String password;

    public Student to(){
        return Student.builder().
                 name(this.name).
                 address(this.address).
                 contact(this.contact).
                 email(this.email).
                 studentType(StudentType.ACTIVE).
                 build();
    }

}
