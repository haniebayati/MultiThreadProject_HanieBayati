package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Account;
import com.example.demo.model.Customer;
import com.example.demo.output.OutputCustomer;
import com.example.demo.repository.AccountRepository;
import com.example.demo.repository.CustomerRepository;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;



@Service
public class SpecialCustomersService {

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    CustomerRepository customerRepository;
    
    private List<OutputCustomer> outputCustomers = new ArrayList<>();


    public void getSpecialCustomers() {
    	
        // Find accounts with balance greater than 1000
        List<Account> accountsWithHighBalance = accountRepository.findByBalanceGreaterThan(1000.0);

        // Get the customer IDs from the accounts
        List<Long> customerIds = accountsWithHighBalance.stream()
                .map(Account::getCustomerId)
                .collect(Collectors.toList());

        // Find the customers with those customer IDs
        List<Customer> specialCustomers = customerRepository.findAllByCustomerIdIn(customerIds);
        
     // Create a list of OutputCustomer by combining customer and account information
        outputCustomers = specialCustomers.stream()
            .flatMap(customer -> {
                // Find all accounts associated with the current customer
                List<Account> customerAccounts = accountsWithHighBalance.stream()
                        .filter(acc -> acc.getCustomerId().equals(customer.getCustomerId()))
                        .collect(Collectors.toList());

                // Map each account to a new OutputCustomer object
                return customerAccounts.stream().map(account -> {
                    // Create OutputCustomer object with combined data
                    OutputCustomer outputCustomer = new OutputCustomer();
                    outputCustomer.setCustomerId(customer.getCustomerId());
                    outputCustomer.setName(customer.getName());
                    outputCustomer.setSurname(customer.getSurname());
                    outputCustomer.setNationalId(customer.getNationalId());
                    outputCustomer.setAccountNumber(account.getAccountNumber());
                    outputCustomer.setOpenDate(account.getOpenDate());
                    outputCustomer.setBalance(account.getBalance());

                    //System.out.println(outputCustomer);
                    return outputCustomer;
                });
            })
            // Collect the result into a List
            .collect(Collectors.toList());

        saveCustomersToXML();
        saveCustomersTOJSON();
        
    }


    private void saveCustomersTOJSON() {
        if (!outputCustomers.isEmpty()) {
            // Use JsonMapper for serialization to JSON
            JsonMapper jsonMapper = new JsonMapper();

            // Register the module to support Java 8 Date/Time types
            jsonMapper.registerModule(new JavaTimeModule());

            try {
                // Specify a path to save the file in the project's main directory
                File outputRecordFile = new File("src/main/java/com/example/demo/output/OutputCustomers.json");

                // Write output records to the file with pretty formatting
                jsonMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputRecordFile, outputCustomers);

                System.out.println("Saved output records to OutputCustomers.json"); // Display the full path of the file
            } catch (IOException e) {
                System.err.println("Error writing output records to file: " + e.getMessage());
                e.printStackTrace(); // Display more details about the error
            }
        } else {
            System.out.println("Output Customer Record is Empty.");
        }
    }


    private void saveCustomersToXML() {
        if (!outputCustomers.isEmpty()) {
            // Use XmlMapper for serialization to XML
            XmlMapper xmlMapper = new XmlMapper();

            // Register the module to support Java 8 Date/Time types
            xmlMapper.registerModule(new JavaTimeModule());

            try {
                // Specify the output XML file path
                File outputRecordFile = new File("src/main/java/com/example/demo/output/OutputCustomers.xml");

                // Write output records to the file with pretty formatting
                xmlMapper.writerWithDefaultPrettyPrinter()
                    .writeValue(outputRecordFile, outputCustomers);

                System.out.println("Saved output records to OutputCustomers.xml");
            } catch (IOException e) {
                System.err.println("Error writing output records to file: " + e.getMessage());
                e.printStackTrace(); // Display more details about the error
            }
        } else {
            System.out.println("Output Customer Record is Empty.");
        }
    }


    
}

