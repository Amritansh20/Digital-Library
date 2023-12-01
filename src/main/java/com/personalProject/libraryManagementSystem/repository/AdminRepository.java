package com.personalProject.libraryManagementSystem.repository;

import com.personalProject.libraryManagementSystem.modals.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RestController;

@Repository
public interface AdminRepository extends JpaRepository<Admin,Integer> {
    Admin findByContact(String contact);
}
