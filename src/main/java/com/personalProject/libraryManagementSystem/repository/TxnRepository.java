package com.personalProject.libraryManagementSystem.repository;

import com.personalProject.libraryManagementSystem.modals.Txn;
import com.personalProject.libraryManagementSystem.modals.TxnStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface TxnRepository  extends JpaRepository<Txn,Integer> {
    Txn findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc(String studentContact, String bookNo, TxnStatus status);
}
