package com.personalProject.libraryManagementSystem.repository;

import com.personalProject.libraryManagementSystem.modals.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


public interface AuthorRepository extends JpaRepository<Author,Integer> {

    @Query(value="select * from author where email = :email",nativeQuery = true)
    Author getAuthorWithMailAddress(String email);
}
