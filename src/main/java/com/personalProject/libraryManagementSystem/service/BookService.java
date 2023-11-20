package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.modals.*;
import com.personalProject.libraryManagementSystem.repository.AuthorRepository;
import com.personalProject.libraryManagementSystem.repository.BookRepository;
import com.personalProject.libraryManagementSystem.requests.CreateBookRequest;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    public void createBook(CreateBookRequest createBookRequest) {
        Book book = createBookRequest.to();

        Author authorFromDb = authorRepository.getAuthorWithMailAddress(book.getAuthor().getEmail());
        if (authorFromDb == null) {
            authorFromDb = authorRepository.save(book.getAuthor());
        }

        bookRepository.save(book);
    }

    public void createUpdate(Book book){
        bookRepository.save(book);
    }

    public List<Book> findBooks(BookFilterType bookFilterType, String value, OperationType operationType) {
        switch (operationType) {
            case EQUALS:
                switch (bookFilterType) {
                    case BOOK_NO:
                        return bookRepository.findByBookNo(value);
                    case AUTHOR_NAME:
                        return bookRepository.findByAuthorName(value);
                    case BOOK_TYPE:
                        return bookRepository.findByBookType(BookType.valueOf(value));
                    case COST:
                        return bookRepository.findByCost(Integer.valueOf(value));
                }
            case GREATER_THAN:
                switch (bookFilterType) {
                    case COST:
                        return bookRepository.findByCostGreaterThan(Integer.valueOf(value));
                }
            case LESS_THAN:
                switch (bookFilterType) {
                    case COST:
                        return bookRepository.findByCostLessThan(Integer.valueOf(value));
                }
            default:
               return new ArrayList<>();
        }
    }
}
