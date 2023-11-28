package com.personalProject.libraryManagementSystem.modals;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString
@Builder
public class Book implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 30)
    private String name;

    private int cost;

    @JoinColumn
    @ManyToOne
//    @JsonIgnoreProperties("bookList")
    @JsonIgnore
    private Author author;

    private String bookNo;

    @Enumerated(value=EnumType.STRING)
    private BookType bookType;

    @JoinColumn
    @ManyToOne
    @JsonIgnoreProperties("bookList")
    private Student student;
}
