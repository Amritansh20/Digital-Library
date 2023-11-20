package com.personalProject.libraryManagementSystem.requests;

import com.personalProject.libraryManagementSystem.modals.Author;
import com.personalProject.libraryManagementSystem.modals.Book;
import com.personalProject.libraryManagementSystem.modals.BookType;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class CreateBookRequest {

    private String bookName;
    private int cost;
    private String bookNo;
    private BookType bookType;

    private String authorName;
    private String authorEmail;

    public Book to(){

        Author authorData = Author.builder()
                .name(this.authorName)
                .email(this.authorEmail)
                .build();

        return Book.builder()
                .name(this.bookName)
                .cost(this.cost)
                .bookNo(this.bookNo)
                .bookType(this.bookType)
                .author(authorData)
                .build();
    }



}
