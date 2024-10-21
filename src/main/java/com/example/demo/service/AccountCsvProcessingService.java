package com.example.demo.service;

import com.example.demo.error.ErrorRecords;
import com.example.demo.model.Account;
import com.example.demo.model.AccountType;
import com.example.demo.repository.AccountRepository;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.json.JsonMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.core.io.ClassPathResource;
import java.io.InputStreamReader;
import java.time.LocalDate;

import com.example.demo.validation.AccountValidation;

@Service
public class AccountCsvProcessingService {

    @Autowired
    private AccountRepository accountRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);
    private final List<ErrorRecords> errorRecords = new ArrayList<>(); // List of error records
    private final AccountValidation accountValidation = new AccountValidation(); // Create an instance of the validation class

    public void processCsvFile(String csvFilePath) {
        try (CSVReader reader = new CSVReader(new InputStreamReader(new ClassPathResource(csvFilePath).getInputStream()))) {
            String[] line;

            while ((line = reader.readNext()) != null) {
                String[] finalLine = line; // Define a final variable

                // Submit task to executorService for saving to the database
                executorService.submit(() -> {
                    try {
                        saveToDatabase(finalLine); // Use finalLine
                        // System.out.println(errorRecords);
                    } catch (Exception e) {
                        // Handle error during saving
                        // System.err.println("Error saving account: " + e.getMessage());
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
        if (line[0].equals("recordNumber")) {
            // This line is a header, so ignore it
            return;
        }

        Account account = new Account();
        try {
            account.setRecordNumber(Long.parseLong(line[0])); // Convert to number
            account.setAccountNumber(line[1]);
            account.setAccountType(AccountType.valueOf(line[2]));
            account.setCustomerId(Long.parseLong(line[3]));
            account.setAccountLimit(Double.parseDouble(line[4]));
            account.setBalance(Double.parseDouble(line[5]));
            account.setOpenDate(LocalDate.parse(line[6].trim()));

            // Validate the record
            List<ErrorRecords> validationErrors = accountValidation.validate(account);
            if (!validationErrors.isEmpty()) {
                // If validation has errors, add the errors to the error list
                validationErrors.forEach(error -> errorRecords.add(error));
                return; // If there are errors, do not save the record
            }

            // Save to database
            accountRepository.save(account);

        } catch (IllegalArgumentException e) {
            // System.err.println("Error saving account: " + e.getMessage());
            // Add error to the errorRecords list in case of an exception
            ErrorRecords error = new ErrorRecords();
            error.setFileName("accounts.csv");
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
                File errorRecordFile = new File("src/main/java/com/example/demo/error/AccountErrorRecords.json");

                // Write error records to the file with pretty formatting
                jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(errorRecordFile, errorRecords);

                System.out.println("Saved error records to AccountErrorRecords.json"); // Display the full path of the file
            } catch (IOException e) {
                System.err.println("Error writing error records to file: " + e.getMessage());
                e.printStackTrace(); // Display more details about the error
            }
        } else {
            System.out.println("Account Error Record is Empty.");
        }
    }
}
