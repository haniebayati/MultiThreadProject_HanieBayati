package com.example.demo.service;

import com.example.demo.error.ErrorRecords;
import com.example.demo.model.Customer;
import com.example.demo.repository.CustomerRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStreamReader;

import com.example.demo.validation.CustomerValidation;

@Service
public class CustomerCsvProcessingService {

    @Autowired
    private CustomerRepository customerRepository; 

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final List<ErrorRecords> errorRecords = new ArrayList<>(); // List of error records
    private final CustomerValidation customerValidation = new CustomerValidation(); // Create an instance of the validation class

    public void processCsvFile(String csvFilePath) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(csvFilePath).getInputStream()))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                String[] finalLine = line; // Define a final variable

                // Submit task to executorService for saving to the database
                executorService.submit(() -> {
                    try {
                        saveToDatabase(finalLine); // Use finalLine
                    } catch (Exception e) {
                        // Handle error during saving
                        // System.err.println("Error saving customer: " + e.getMessage());
                    }
                });
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
            // Handle error while reading the file
        } finally {
            executorService.shutdown(); // Ensure executorService is closed
            try {
                if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
                    executorService.shutdownNow(); // If still running after 60 seconds
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
            }
            
            saveErrorRecords(); // Save error records to JSON file
        }
    }

    private void saveToDatabase(String[] line) throws NumberFormatException {
        // It is assumed that each line contains:
        // recordNumber, customerId, name, surname, address, zipCode, nationalId, birthDate
        if (line[0].equals("recordNumber")) {
            // This line is a header, so ignore it
            return;
        }

        Customer customer = new Customer();
        try {
            customer.setRecordNumber(Long.parseLong(line[0])); // Convert to number
            customer.setCustomerId(Long.parseLong(line[1]));
            customer.setName(line[2].trim());
            customer.setSurname(line[3].trim());
            customer.setAddress(line[4].trim());
            customer.setZipCode(line[5].trim());
            customer.setNationalId(line[6].trim());
            customer.setBirthDate(LocalDate.parse(line[7].trim())); // Convert date to LocalDate

            // Validate the record
            List<ErrorRecords> validationErrors = customerValidation.validateCustomer(customer);
            if (!validationErrors.isEmpty()) {
                // If validation has errors, add the errors to the error list
                validationErrors.forEach(error -> errorRecords.add(error));
                return; // If there are errors, do not save the record
            }

            // Save to database
            customerRepository.save(customer);
            
        } catch (IllegalArgumentException e) {
            // System.err.println("Error saving customer: " + e.getMessage());
            // Add error to the errorRecords list in case of an exception
            ErrorRecords error = new ErrorRecords();
            error.setFileName("customers.csv");
            error.setRecordNumber(Long.parseLong(line[0]));
            error.setErrorCode("PARSE_ERROR");
            error.setErrorClassificationName("Parse Failure");
            error.setErrorDescription(e.getMessage());
            error.setErrorDate(new Date());
            errorRecords.add(error);
        }
    }

    private void saveErrorRecords() {
        if (!errorRecords.isEmpty()) {
            JsonMapper jsonMapper = new JsonMapper();
            
            try {
                // Specify a path to save the file in the project's main directory
                File errorRecordFile = new File("src/main/java/com/example/demo/error/CustomerErrorRecords.json");
                
                // Write error records to the file with pretty formatting
                jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(errorRecordFile, errorRecords);
                System.out.println("Saved error records to CustomerErrorRecord.json");
            } catch (IOException e) {
                System.err.println("Error writing error records to file: " + e.getMessage());
            }
        } else {
            System.out.println("Customer Error Record is Empty.");
        }
    }
}
