package com.example.demo.validation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.example.demo.error.ErrorRecords;
import com.example.demo.model.Customer;

public class CustomerValidation {
	
    private static final String FILE_NAME = "Customers.csv";
    private static final String VALIDATION_ERROR = "Validation Error";
	
    // customer validation
    public List<ErrorRecords> validateCustomer(Customer customer) {
    	
        List<ErrorRecords> errorList = new ArrayList<>();
        ErrorRecords errorLog;
        
        // check null validation
        if (customer.getCustomerId() == null || customer.getName() == null
                || customer.getSurname() == null || customer.getAddress() == null
                || customer.getBirthDate() == null || customer.getZipCode() == null
                || customer.getNationalId() == null || customer.getRecordNumber() == null ) {
    		errorLog = new ErrorRecords(FILE_NAME, customer.getRecordNumber(), "NULL_FIELD", "Field cannot be null", VALIDATION_ERROR, new Date());
    		errorList.add(errorLog);
        }

        // check birth year > 1995 validation
        LocalDate birthDate = customer.getBirthDate();
        if (birthDate.getYear() <= 1995) {
        	errorLog = new ErrorRecords(FILE_NAME, customer.getRecordNumber(), "INVALID_BIRTH_YEAR", "Birth year is before 1995", VALIDATION_ERROR, new Date());
        	errorList.add(errorLog);
        }

        // check national id validation
        if (customer.getNationalId().length() != 10) {
        	errorLog = new ErrorRecords(FILE_NAME, customer.getRecordNumber(), "INVALID_NATIONAL_ID", "National ID must be 10 digits", VALIDATION_ERROR, new Date());
        	errorList.add(errorLog);
        }
        
		return errorList;
    }

}
