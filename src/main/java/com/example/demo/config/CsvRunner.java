package com.example.demo.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.demo.service.AccountCsvProcessingService;
import com.example.demo.service.CustomerCsvProcessingService;
import com.example.demo.service.SpecialCustomersService;

@Component
public class CsvRunner implements CommandLineRunner {

    private final AccountCsvProcessingService accountCsvProcessingService;
    private final CustomerCsvProcessingService customerCsvProcessingService;
    private final SpecialCustomersService specialCustomersServic;

    public CsvRunner(AccountCsvProcessingService accountCsvProcessingService, 
                     CustomerCsvProcessingService customerCsvProcessingService,
                     SpecialCustomersService specialCustomersServic) {
        this.accountCsvProcessingService = accountCsvProcessingService;
        this.customerCsvProcessingService = customerCsvProcessingService;
        this.specialCustomersServic = specialCustomersServic;
    }

    @Override
    public void run(String... args) throws Exception {
        String accountCsvFilePath = "data/accounts.csv"; // Path to the accounts CSV file
        String customerCsvFilePath = "data/customers2.csv"; // Path to the customers CSV file

        // Process the accounts CSV file
        accountCsvProcessingService.processCsvFile(accountCsvFilePath);

        // Process the customers CSV file
        customerCsvProcessingService.processCsvFile(customerCsvFilePath);
        
        // Process the Special Customer Service
        specialCustomersServic.getSpecialCustomers();
    }
}


