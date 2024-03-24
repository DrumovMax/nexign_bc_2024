package com.nexign.bootcamp.emulator.udr;

import com.nexign.bootcamp.emulator.model.Subscriber;
import com.nexign.bootcamp.emulator.repository.SubscriberRepository;
import com.nexign.bootcamp.emulator.service.UdrService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(properties = "spring.shell.interactive.enabled=false")
public class UdrTests {

    @Value("${directory.report.name}")
    private String dirReportName;

    @Resource
    private SubscriberRepository subscriberRepository;

    @Resource
    private UdrService udrService;

    @Test
    void testAnnualReport() throws IOException {
        Path reportPath = Paths.get(dirReportName + "/annual_report_2023.json");

        if (Files.exists(reportPath)) Files.delete(reportPath);

        udrService.generateReport();

        assertTrue(Files.exists(reportPath));
        assertTrue(Files.size(reportPath) > 0);
    }

    @Test
    void testAnnualReportByPhoneNumber() throws IOException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        Random random = new Random();

        Subscriber testSubscriber = subscribers.get(random.nextInt(subscribers.size()));

        Path reportPath = Paths.get("%s/annual_report_%d_2023.json".formatted(dirReportName, testSubscriber.getPhoneNumber()));

        if (Files.exists(reportPath)) Files.delete(reportPath);

        udrService.generateReport(testSubscriber.getPhoneNumber());

        assertTrue(Files.exists(reportPath));
        assertTrue(Files.size(reportPath) > 0);
    }

    @Test
    void testAnnualReportByPhoneNumberAllSubscriber() {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        subscribers.forEach(subscriber ->
        {
            Path reportPath = Paths.get("%s/annual_report_%d_2023.json".formatted(dirReportName, subscriber.getPhoneNumber()));

            if (Files.exists(reportPath)) {
                try {
                    Files.delete(reportPath);

                    udrService.generateReport(subscriber.getPhoneNumber());

                    assertTrue(Files.exists(reportPath));
                    assertTrue(Files.size(reportPath) > 0);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }

    @Test
    void testMonthReportByPhoneNumber() throws IOException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        Random random = new Random();

        int testMonth = random.nextInt(12) + 1;
        Subscriber testSubscriber = subscribers.get(random.nextInt(subscribers.size()));

        Path reportPath = Paths.get("%s/month_%d_report_%d_2023.json".formatted(dirReportName, testMonth, testSubscriber.getPhoneNumber()));

        if (Files.exists(reportPath)) Files.delete(reportPath);

        udrService.generateReport(testSubscriber.getPhoneNumber(), testMonth);

        assertTrue(Files.exists(reportPath));
        assertTrue(Files.size(reportPath) > 0);
    }

    @Test
    void testAllMonthReportByPhoneNumber() throws IOException {
        List<Subscriber> subscribers = subscriberRepository.findAll();
        Random random = new Random();

        Subscriber testSubscriber = subscribers.get(random.nextInt(subscribers.size()));

        for (int i = 1; i <= 12; i++) {
            Path reportPath = Paths.get("%s/month_%d_report_%d_2023.json".formatted(dirReportName, i, testSubscriber.getPhoneNumber()));

            if (Files.exists(reportPath)) Files.delete(reportPath);

            udrService.generateReport(testSubscriber.getPhoneNumber(), i);

            assertTrue(Files.exists(reportPath));
            assertTrue(Files.size(reportPath) > 0);
        }
    }
}
