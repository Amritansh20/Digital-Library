package com.personalProject.libraryManagementSystem.controller;

import com.personalProject.libraryManagementSystem.modals.OperationType;
import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.StudentFilterType;
import com.personalProject.libraryManagementSystem.modals.StudentType;
import com.personalProject.libraryManagementSystem.requests.CreateStudentRequest;
import com.personalProject.libraryManagementSystem.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/find")
    public List<Student> findStudent(@RequestParam("filter")StudentFilterType studentFilterType,
                                     @RequestParam("value") String value,
                                     @RequestParam("operation")OperationType operationType){

        return studentService.findStudent(studentFilterType,value,operationType);
    }

    @PostMapping("/create")
    public Student createStudent(@RequestBody CreateStudentRequest createStudentRequest){
        return studentService.createStudent(createStudentRequest);
    }
}
