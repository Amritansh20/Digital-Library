package com.personalProject.libraryManagementSystem.service;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.modals.*;
import com.personalProject.libraryManagementSystem.repository.TxnRepository;
import com.personalProject.libraryManagementSystem.requests.CreateReturnTxnRequest;
import com.personalProject.libraryManagementSystem.requests.CreateTxnRequest;
import com.personalProject.libraryManagementSystem.response.TxnSettlementResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TxnService {

    @Autowired
    StudentService studentService;

   @Autowired
    TxnRepository txnRepository;

   @Autowired
    BookService bookService;

   public Student findStudent(String studentContact) throws TxnServiceException {
       List<Student> students = studentService.findStudent(StudentFilterType.CONTACT, studentContact, OperationType.EQUALS);
       if(students == null || students.isEmpty()){
            throw new TxnServiceException("Student does not exists");
       }

       return students.get(0);
   }

   public Book findBook(String bookNo) throws TxnServiceException{
       List<Book> books = bookService.findBooks(BookFilterType.BOOK_NO,bookNo,OperationType.EQUALS);
       if(books == null || books.isEmpty() || books.get(0).getStudent()!=null){
           throw new TxnServiceException("Book does not exists");
       }
       return books.get(0);
   }

    public Book findBookForReturn(String bookNo) throws TxnServiceException{
        List<Book> books = bookService.findBooks(BookFilterType.BOOK_NO,bookNo,OperationType.EQUALS);
        if(books == null || books.isEmpty()){
            throw new TxnServiceException("Book does not exists");
        }
        return books.get(0);
    }

   @Transactional(rollbackOn = {TxnServiceException.class})
   public String createTxn(CreateTxnRequest createTxnRequest) throws TxnServiceException {
        Student student = findStudent(createTxnRequest.getStudentContact());
        Book book = findBook(createTxnRequest.getBookNo());

        Txn txn = Txn.builder().
                    student(student).
                    book(book).
                    TxnId(UUID.randomUUID().toString()).
                    paidCost(createTxnRequest.getPaidcost()).
                    txnStatus(TxnStatus.ISSUED).
                    build();

        book.setStudent(student);
        bookService.createUpdate(book);
        return txnRepository.save(txn).getTxnId();
   }

   @Transactional
    public TxnSettlementResponse returnBook(CreateReturnTxnRequest createReturnTxnRequest) throws TxnServiceException {
       Student student = findStudent(createReturnTxnRequest.getStudentContact());
       Book book = findBookForReturn(createReturnTxnRequest.getBookNo());

       Txn txn = txnRepository.
               findByStudent_ContactAndBook_BookNoAndTxnStatusOrderByCreatedOnDesc(student.getContact(),book.getBookNo(),TxnStatus.ISSUED);

       int settlementAmount = calculateSettlementAmount(txn);
       txn.setTxnStatus(settlementAmount == txn.getPaidCost()? TxnStatus.RETURNED : TxnStatus.FINED);
       txn.setPaidCost(settlementAmount);
       txnRepository.save(txn);

       //making book available
       book.setStudent(null);
       bookService.createUpdate(book);
       return TxnSettlementResponse.builder().
               TxnId(txn.getTxnId()).
               SettlementAmount(settlementAmount).
               build();

   }

    @Value("${student.valid.days}")
    private String validDays;
    @Value("${student.perday.fine}")
    private Integer finePerDay;
   public int calculateSettlementAmount(Txn txn){
       long issueTime = txn.getCreatedOn().getTime();
       long returnTime = System.currentTimeMillis();

       long diff = returnTime - issueTime;
       int daysPassed = (int) TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
       if(daysPassed > Integer.valueOf(validDays)){
        int amount = (daysPassed - Integer.valueOf(validDays)) * finePerDay;
        return txn.getPaidCost()-amount;
       }
       return txn.getPaidCost();
   }
}
