package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.modals.*;
import com.personalProject.libraryManagementSystem.repository.AuthorRepository;
import com.personalProject.libraryManagementSystem.repository.BookRepository;
import com.personalProject.libraryManagementSystem.requests.CreateBookRequest;
import net.bytebuddy.implementation.MethodDelegation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class BookService {
    @Autowired
    BookRepository bookRepository;
    @Autowired
    AuthorRepository authorRepository;

    @Autowired
    RedisTemplate redisTemplate;

    private static final String BOOK_PREFIX_KEY ="book:";

    public void createBook(CreateBookRequest createBookRequest) {
        Book book = createBookRequest.to();

        Author authorFromDb = authorRepository.getAuthorWithMailAddress(book.getAuthor().getEmail());
        if (authorFromDb == null) {
            authorFromDb = authorRepository.save(book.getAuthor());
        }
        List<Book> list = new ArrayList<>();

        //updating author details(id)
        book.setAuthor(authorFromDb);
        //saving data to db
        bookRepository.save(book);

        //pushing data to redis
        pushDataToRedisByBookNo(book);
        list.add(book);
        pushDataToRedisByAuthorName(list);
        pushDataToRedisByType(book);
        pushDataToRedisByCost(book);


    }

    public void pushDataToRedisByBookNo(Book book){
        redisTemplate.opsForValue().set(BOOK_PREFIX_KEY+book.getBookNo(), book, 10, TimeUnit.MINUTES);
    }

    public void pushDataToRedisByAuthorName(List<Book> book) {
        if (!book.isEmpty()) {
            String authorName = book.get(0).getName();
            redisTemplate.opsForList().leftPush(BOOK_PREFIX_KEY + authorName, book);
            redisTemplate.expire(BOOK_PREFIX_KEY + authorName, 10, TimeUnit.MINUTES);
        }
    }

    public void pushDataToRedisByType(Book book){
        redisTemplate.opsForList().rightPush(BOOK_PREFIX_KEY+book.getBookType(), book);
        redisTemplate.expire(BOOK_PREFIX_KEY+book.getBookType(), 10, TimeUnit.MINUTES);
    }

    public void pushDataToRedisByCost(Book book){
        redisTemplate.opsForList().rightPush(BOOK_PREFIX_KEY+book.getCost(), book);
        redisTemplate.expire(BOOK_PREFIX_KEY+book.getCost(), 10, TimeUnit.MINUTES);
    }
    public void createUpdate(Book book){
        bookRepository.save(book);
    }

    public List<Book> findBooks(BookFilterType bookFilterType, String value, OperationType operationType) {
        switch (operationType) {
            case EQUALS:
                switch (bookFilterType) {
                    case BOOK_NO:
                        //checking in redis
                        Book bookWithPrefix = (Book)redisTemplate.opsForValue().get(BOOK_PREFIX_KEY+value);
                        //if present in redis
                        if(bookWithPrefix!=null){
                            List<Book> list = new ArrayList<>();
                            list.add(bookWithPrefix);
                            return list;
                        }

                        //if not present in redis, getting it from db and pushing in redis
                        List<Book> bookList= bookRepository.findByBookNo(value);
                        pushDataToRedisByBookNo(bookList!=null ? bookList.get(0):null);
                        return bookList;

                    case AUTHOR_NAME:
                        //checking in redis
                        List<Book> bookWithAuthorName = redisTemplate.opsForList().range(BOOK_PREFIX_KEY+value, 0, -1);
                        if(!bookWithAuthorName.isEmpty()){
                            return bookWithAuthorName;
                        }
                         bookWithAuthorName= bookRepository.findByAuthorName(value);
                        pushDataToRedisByAuthorName(!bookWithAuthorName.isEmpty() ? bookWithAuthorName: null);
                        return bookWithAuthorName;
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
