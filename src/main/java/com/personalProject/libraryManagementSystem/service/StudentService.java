package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.modals.OperationType;
import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.StudentFilterType;
import com.personalProject.libraryManagementSystem.repository.StudentRepository;
import com.personalProject.libraryManagementSystem.requests.CreateStudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class StudentService {

    @Autowired
    StudentRepository studentRepository;
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
            return studentRepository.save(student);
        }
        return students.get(0);
    }
}
