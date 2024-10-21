package com.example.demo.output;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;

public class OutputCustomer {
	
    private Long customerId;

    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String surname;
    
    @Column(nullable = false, unique = true)
    private String nationalId;
    
    private String accountNumber;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate openDate; 

    private Double balance;

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getNationalId() {
		return nationalId;
	}

	public void setNationalId(String nationalId) {
		this.nationalId = nationalId;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
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
		return "OutputCustomer [customerId=" + customerId + ", name=" + name + ", surname=" + surname + ", nationalId="
				+ nationalId + ", accountNumber=" + accountNumber + ", openDate=" + openDate + ", balance=" + balance
				+ "]";
	}
    
    

}
