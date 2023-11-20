package com.personalProject.libraryManagementSystem.requests;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateReturnTxnRequest {
    private String studentContact;
    private String bookNo;
}
