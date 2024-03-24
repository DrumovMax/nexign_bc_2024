package com.nexign.bootcamp.emulator.commands;

import com.nexign.bootcamp.emulator.model.Subscriber;
import com.nexign.bootcamp.emulator.repository.SubscriberRepository;
import com.nexign.bootcamp.emulator.service.UdrService;
import jakarta.annotation.Resource;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Component containing shell commands for interacting with the emulator system.
 */
@Service
@ShellComponent
public class ShellCommands {

    /**
     * Service for generating UDR reports.
     */
    @Resource
    private UdrService udrService;

    /**
     * Repository for managing subscriber entities.
     */
    @Resource
    private SubscriberRepository subscriberRepository;

    /**
     * Displays all subscribers from the database.
     */
    @ShellMethod(value = "Display all subscribers from the database", key = "subs")
    public void subs() {
        System.out.println("Current subscribers:");
        System.out.println("_____________________");
        subscriberRepository.findAll().forEach(subscriber -> {
            System.out.printf("| %3d | %d |%n", subscriber.getId(), subscriber.getPhoneNumber());
        });
    }

    /**
     * Generates a report for all subscribers for the entire year.
     *
     * @return a message indicating the success of the operation
     */
    @ShellMethod(value = """
            
            \tGenerate a report for all subscribers for the entire year
            \t\tgenerate-report
            """,
            key = "generate-report")
    public String generateReport () {
        System.out.println("Generating annual report of 2023");
        udrService.generateReport();
        return "Success";
    }

    /**
     * Generates a report for a subscriber by phone number for the entire year.
     *
     * @param msisdn the phone number of the subscriber
     * @return a message indicating the success of the operation or if the subscriber is not found
     */
    @ShellMethod(value = """
            
            \tGenerate a report for a subscriber by phone number for the entire year
            \t\tgenerate-report-sub phoneNumber(Call command subs to explore subscribers phone number)
            \tExample:
            \t\tgenerate-report-sub 79193444074
            """,
            key = "generate-report-sub")
    public String generateReportSub (Long msisdn) {

        Optional<Subscriber> sub = subscriberRepository.findByPhoneNumber(msisdn);
        if (sub.isPresent()) {
            System.out.println("Generating annual report of 2023 for " + msisdn);
            udrService.generateReport(msisdn);
        } else {
            return "Not our subscriber";
        }
        return "Success";
    }

    /**
     * Generates a report for a subscriber by phone number and month for the entire year.
     *
     * @param msisdn the phone number of the subscriber
     * @param month  the month for which the report is generated
     * @return a message indicating the success of the operation or if the subscriber is not found
     */
    @ShellMethod(value =
            """
                    
            \tGenerate a report for a subscriber by phone number and month for the entire year
            \t\tgenerate-month-report-sub phoneNumber(Call command subs to explore subscribers phone number) month(1-12)
            \tExample:
            \t\tgenerate-month-report-sub 79193444074 5
            """,
            key = "generate-month-report-sub")
    public String generateMonthReportSub (Long msisdn, int month) {
        if (month >= 1 && month <= 12) {
            Optional<Subscriber> sub = subscriberRepository.findByPhoneNumber(msisdn);
            if (sub.isPresent()) {
                System.out.println("Generating report by month for " + msisdn);
                udrService.generateReport(msisdn, month);
            } else {
                return "Not our subscriber";
            }
            return "Success";
        } else {
            return "Month should be in range 1-12";
        }
    }
}
