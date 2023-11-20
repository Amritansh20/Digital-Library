package com.personalProject.libraryManagementSystem.response;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TxnSettlementResponse {
    private String TxnId;
    private Integer SettlementAmount;
}
