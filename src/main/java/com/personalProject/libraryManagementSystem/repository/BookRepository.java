package com.personalProject.libraryManagementSystem.repository;

import com.personalProject.libraryManagementSystem.modals.Book;
import com.personalProject.libraryManagementSystem.modals.BookType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface BookRepository extends JpaRepository<Book,Integer> {

    List<Book> findByBookNo(String bookNo);
    List<Book> findByAuthorName(String authorName);

    List<Book> findByBookType(BookType bookType);

    List<Book> findByCost(Integer cost);

    List<Book> findByCostGreaterThan(Integer cost);

    List<Book> findByCostLessThan(Integer cost);



}
