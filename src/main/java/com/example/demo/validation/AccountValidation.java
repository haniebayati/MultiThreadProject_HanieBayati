package com.example.demo.validation;

import com.example.demo.error.ErrorRecords;
import com.example.demo.model.Account;
import com.example.demo.model.AccountType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//import com.example.demo.model.Customer;

//account validation
public class AccountValidation {

    private static final String FILE_NAME = "Accounts.csv";
    private static final String VALIDATION_ERROR = "Validation Error";
     

	public List<ErrorRecords> validate(Account account) {
    	
        List<ErrorRecords> errorList = new ArrayList<>();
    //    List<Customer> customers = new ArrayList<>();
        ErrorRecords errorLog;

        // Check for null values
        if (account.getAccountNumber() == null || account.getAccountType() == null ||
            account.getCustomerId() == null || account.getAccountLimit() == null ||
            account.getBalance() == null || account.getRecordNumber() == null) {
        		errorLog = new ErrorRecords(FILE_NAME, account.getRecordNumber(), "NULL_FIELD", "Field cannot be null", VALIDATION_ERROR, new Date());
        		errorList.add(errorLog);    
        }
        
        // Check account balance >= account limit
        if (account.getBalance() != null && account.getAccountLimit() != null && account.getBalance() < account.getAccountLimit()) {
			errorLog = new ErrorRecords(FILE_NAME, account.getRecordNumber(), "INVALID_BALANCE", "Balance is less than account limit", VALIDATION_ERROR, new Date());
			errorList.add(errorLog);
        }

        // Check valid account type
        if (account.getAccountType() != null && !((account.getAccountType().equals(AccountType.savings)) || 
            (account.getAccountType().equals(AccountType.recurringDeposit)) || 
            (account.getAccountType().equals(AccountType.fixedDepositAccount)))) {
			errorLog = new ErrorRecords(FILE_NAME, account.getRecordNumber(), "INVALID_ACCOUNT_TYPE", "Invalid account type", VALIDATION_ERROR, new Date());
			errorList.add(errorLog);
        }    

        // Check account number format (must be 22 digits)
        if (account.getAccountNumber() != null && !account.getAccountNumber().matches("\\d{22}")) {
			errorLog = new ErrorRecords(FILE_NAME, account.getRecordNumber(), "INVALID_ACCOUNT_NUMBER", "Account number must be 22 digits", VALIDATION_ERROR, new Date());
			errorList.add(errorLog);
        }
        
        // check relation between account and customer
   /*     boolean customerExists = customers.stream()
                .anyMatch(customer -> customer.getCustomerId().equals(account.getCustomerId()));
        if (!customerExists) {
        	errorLog = new ErrorRecords(FILE_NAME, account.getRecordNumber(), "NO_MATCHING_CUSTOMER", "No matching customer found for account", "Validation Error", new Date());
        	errorList.add(errorLog);
        }  */

        return errorList;
    }
}


