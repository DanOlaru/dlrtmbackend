package org.longmoneyoffshore.dlrtmweb.service;


import org.longmoneyoffshore.dlrtmweb.repository.TransactionDao;
import org.longmoneyoffshore.dlrtmweb.entities.models.entity.Transaction;

import java.util.Collection;
import java.util.Date;

public class TransactionService {

    private TransactionDao myTransactionDao;

    //constructor
    public TransactionService (TransactionDao transactionDao) { this.myTransactionDao = transactionDao; }

    public Collection<Transaction> getAllTransactions() {
        return this.myTransactionDao.getAllTransactions();
    }

    public Collection<Transaction> getTransactionsByField(Object field) {
        return this.myTransactionDao.getTransactionsByField(field);
    }

    public Collection<Transaction> getTransactionsByDate (Date date) {
        return this.myTransactionDao.getAllTransactionsByDate(date);
    }

    public Transaction getTransactionById (String id) {
        return this.myTransactionDao.getTransactionById(id);
    }


    public void removeTransactionById (String id) {
        this.myTransactionDao.removeTransactionById(id);
    }


    public void updateTransaction (Transaction transaction) {

        this.myTransactionDao.updateTransaction(transaction);
    }

    public void insertTransaction (Transaction transaction) {
        this.myTransactionDao.insertTransaction(transaction);
    }

}

