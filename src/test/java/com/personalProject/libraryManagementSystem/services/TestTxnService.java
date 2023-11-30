package com.personalProject.libraryManagementSystem.services;

import com.personalProject.libraryManagementSystem.customException.TxnServiceException;
import com.personalProject.libraryManagementSystem.modals.Student;
import com.personalProject.libraryManagementSystem.modals.Txn;
import com.personalProject.libraryManagementSystem.repository.TxnRepository;
import com.personalProject.libraryManagementSystem.service.BookService;
import com.personalProject.libraryManagementSystem.service.StudentService;
import com.personalProject.libraryManagementSystem.service.TxnService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import java.text.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

@SpringBootTest
@RunWith(MockitoJUnitRunner.class)
public class TestTxnService {

    @Mock
    StudentService studentService;

    @Mock
    BookService bookService;

    @Mock
    TxnRepository txnRepository;

    @InjectMocks
    TxnService txnService;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(new TestTxnService());
        ReflectionTestUtils.setField(txnService, "validDays" ,"14");
        ReflectionTestUtils.setField(txnService, "finePerDay" ,2);
    }

    @Test(expected = TxnServiceException.class)
    public void testFindStudentWithNullCheck() throws TxnServiceException {
        Mockito.when(studentService.findStudent(any(),eq("7004363155"),any())).thenReturn(null);
//        Mockito.when(studentService.findStudent(any(),eq("7004363155"),any())).thenReturn(new ArrayList<>());
        txnService.findStudent("7004363155");
    }

    @Test(expected = TxnServiceException.class)
    public void testFindStudentWithEmptyResponse() throws TxnServiceException {
        Mockito.when(studentService.findStudent(any(),any(),any())).thenReturn(new ArrayList<>());
        txnService.findStudent("7004363144");
    }

    @Test
    public void testFindStudentWithSuccessResponse() throws TxnServiceException {
        List<Student> list = new ArrayList<>();
        Student s1 =Student.builder().name("student1").build();
        Student s2 =Student.builder().name("student2").build();
        list.add(s1);
        list.add(s2);
        Mockito.when(studentService.findStudent(any(),any(),any())).thenReturn(list);
        Assert.assertEquals(list.get(0), txnService.findStudent("7004363155"));
    }

    @Test
    public void testCalculateSettlementAmount() throws ParseException {
        DateFormat dtf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        String dateString = "2023-11-14 15:59:53";
        Txn txn = Txn.builder().createdOn(dtf.parse(dateString)).paidCost(20).build();
        int amount = txnService.calculateSettlementAmount(txn);
        Assert.assertEquals(18,amount);
        //amount may vary due to date
    }
}
