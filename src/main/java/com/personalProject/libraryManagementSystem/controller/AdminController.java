package com.personalProject.libraryManagementSystem.controller;

import com.personalProject.libraryManagementSystem.modals.Admin;
import com.personalProject.libraryManagementSystem.requests.CreateAdminRequest;
import com.personalProject.libraryManagementSystem.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    AdminService adminService;

    @PostMapping("/create")
    public Admin createAdmin(@RequestBody @Valid CreateAdminRequest createAdminRequest){
        return adminService.create(createAdminRequest);
    }
}
