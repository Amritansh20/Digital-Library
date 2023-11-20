package com.personalProject.libraryManagementSystem.controller;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.requests.CreateReturnTxnRequest;
import com.personalProject.libraryManagementSystem.requests.CreateTxnRequest;
import com.personalProject.libraryManagementSystem.response.GenericResponse;
import com.personalProject.libraryManagementSystem.response.TxnSettlementResponse;
import com.personalProject.libraryManagementSystem.service.TxnService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("/txn")
public class TxnController {

    @Autowired
    TxnService txnService;
    CreateReturnTxnRequest createReturnTxnRequest;

    @PostMapping("/createTxn")
    public ResponseEntity<GenericResponse<String>> createTxn(@RequestBody @Valid CreateTxnRequest createTxnRequest) throws TxnServiceException {
        String txnId = txnService.createTxn(createTxnRequest);
        GenericResponse genericResponse = GenericResponse.builder().error(null).data(txnId).status(HttpStatus.OK).build();
        return new ResponseEntity<>(genericResponse,HttpStatus.OK);

    }

    @PostMapping("/return")
    public TxnSettlementResponse returnBook(@RequestBody CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException {
        this.createReturnTxnRequest = createReturnTxnRequest;
        return txnService.returnBook(createReturnTxnRequest);
    }
}
