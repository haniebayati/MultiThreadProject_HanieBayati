package com.example.demo.model;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;

@Entity
public class Account {

    @Id
    private Long recordNumber;

    private String accountNumber;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private Long customerId;

    private Double accountLimit;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; 

    private Double balance;


    public Long getRecordNumber() {
        return recordNumber;
    }

    public void setRecordNumber(Long recordNumber) {
        this.recordNumber = recordNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Double getAccountLimit() {
        return accountLimit;
    }

    public void setAccountLimit(Double accountLimit) {
        this.accountLimit = accountLimit;
    }

    public LocalDate getOpenDate() {
        return openDate;
    }

    public void setOpenDate(LocalDate openDate) {
        this.openDate = openDate;
    } 

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Account{" +
                "recordNumber=" + recordNumber +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType=" + accountType +
                ", customerId=" + customerId +
                ", accountLimit=" + accountLimit +
                ", openDate=" + openDate +
                ", balance=" + balance +
                '}';
    }
}

