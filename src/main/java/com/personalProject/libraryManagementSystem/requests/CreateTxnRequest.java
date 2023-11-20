package com.personalProject.libraryManagementSystem.requests;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class CreateTxnRequest {
    @NotBlank(message = "Student contact must not be blank")
    private String studentContact;
    @NotBlank(message = "book number must not be blank")
    private String bookNo;
    @NotNull(message = "Paid Amount should not be null")
    @Positive(message = "Paid Amount must be positive")
    private Integer paidcost;
}
