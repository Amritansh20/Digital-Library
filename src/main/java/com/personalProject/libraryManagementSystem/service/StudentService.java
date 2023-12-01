package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.modals.OperationType;
import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.StudentFilterType;
import com.personalProject.libraryManagementSystem.modals.User;
import com.personalProject.libraryManagementSystem.repository.StudentRepository;
import com.personalProject.libraryManagementSystem.repository.UserRepository;
import com.personalProject.libraryManagementSystem.requests.CreateStudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${student.authority}")
    private String studentAuthority;

    @Autowired
    UserRepository userRepository;

    public List<Student> findStudent(StudentFilterType studentFilterType, String value, OperationType operationType) {
        switch(operationType){
            case EQUALS:
                switch (studentFilterType){
                    case EMAIL:
                        return studentRepository.findByEmail(value);
                    case CONTACT:
                        return studentRepository.findByContact(value);
                }
            default:
                return new ArrayList<>();
        }
    }

    public Student createStudent(CreateStudentRequest createStudentRequest) {
        List<Student> students = findStudent(StudentFilterType.CONTACT,createStudentRequest.getContact(),OperationType.EQUALS);

        if(students==null || students.isEmpty()){
            Student student = createStudentRequest.to();
            User user = (User.builder().
                    contact(student.getContact())).
                    password(passwordEncoder.encode(createStudentRequest.getPassword())).
                    authorities(studentAuthority).
                    build();
            user = userRepository.save(user);
            student.setUser(user);
            return studentRepository.save(student);
        }
        return students.get(0);
    }
}
