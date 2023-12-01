package com.personalProject.libraryManagementSystem.controller;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.modals.Book;
import com.personalProject.libraryManagementSystem.modals.BookFilterType;
import com.personalProject.libraryManagementSystem.modals.OperationType;
import com.personalProject.libraryManagementSystem.requests.CreateBookRequest;
import com.personalProject.libraryManagementSystem.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/book")
public class BookController {
    @Autowired
    BookService bookService;

    @PostMapping("/create")
    public void createBook(@RequestBody CreateBookRequest createBookRequest){
        bookService.createBook(createBookRequest);
    }

    @GetMapping("/find")
    public List<Book> findBooks(@RequestParam("filter")BookFilterType bookFilterType,
                                @RequestParam("value") String value,
                                @RequestParam("operation")OperationType operationType){
        return bookService.findBooks(bookFilterType,value,operationType);

    }

}
