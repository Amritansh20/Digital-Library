package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.modals.Admin;
import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.User;
import com.personalProject.libraryManagementSystem.repository.AdminRepository;
import com.personalProject.libraryManagementSystem.repository.UserRepository;
import com.personalProject.libraryManagementSystem.requests.CreateAdminRequest;
import com.personalProject.libraryManagementSystem.requests.CreateStudentRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${admin.authority}")
    private String adminAuthority;

    public Admin create(CreateAdminRequest createAdminRequest) {
        Admin admin = adminRepository.findByContact(createAdminRequest.getContact());
        if(admin != null){
            return admin;
        }
        admin = createAdminRequest.to();

        User user = (User.builder().
                contact(admin.getContact())).
                password(passwordEncoder.encode(createAdminRequest.getPassword())).
                authorities(adminAuthority).
                build();
        user = userRepository.save(user);
        admin.setUser(user);
        return  adminRepository.save(admin);
    }
}
