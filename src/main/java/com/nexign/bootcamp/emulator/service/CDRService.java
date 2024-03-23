package com.nexign.bootcamp.emulator.service;

import com.nexign.bootcamp.emulator.model.CDR;
import com.nexign.bootcamp.emulator.model.Subscriber;
import com.nexign.bootcamp.emulator.repository.CDRRepository;
import com.nexign.bootcamp.emulator.repository.SubscriberRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

@Service
public class CDRService {
    @Resource
    private CDRRepository cdrRepository;

    @Resource
    private SubscriberRepository subscriberRepository;

    Logger log = LoggerFactory.getLogger(CDRService.class);

    public void initCDRFiles() {
        try {
            Path directory = Paths.get("cdr_files");
            if (Files.exists(directory)) {
                deleteDirectory(directory);
            }
            for (int i = 1; i <= 12; i++) {
                String pathname = "cdr_files/cdr_" + i + ".txt";
                File file = new File(pathname);
                if (file.getParentFile() != null && !file.getParentFile().exists()) {
                    if (file.getParentFile().mkdir()) {
                        System.out.println("Recreate directory");
                    }
                }
                if (file.createNewFile()) {
                    System.out.println("Create file " + pathname);
                }
            }
        } catch (IOException e) {
            log.error("Ошибка при создании файла: " + e.getMessage());
        }
    }

    private long countNextUnixTimeMonth (long currentUnixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentUnixTime), ZoneOffset.UTC);

        LocalDateTime newDateTime = dateTime.plusMonths(1);
        System.out.println(newDateTime);

        return newDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    private long randNextCall (long prevCallUnixTime, long limitMonthUnixTime) {
        int min = 28800, max = 57600;
        Random random = new Random();
        long newCallUnixTime = prevCallUnixTime + random.nextInt(max - min) + min;

        return Math.min(newCallUnixTime, limitMonthUnixTime);
    }

    private long randCallTime (long startCallUnixTime) {
        Random random = new Random();
        return startCallUnixTime + random.nextInt(1800);
    }


    @Transactional
    public void emulateSwitch () {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("01/01/2023 00:00:00", formatter);
        dateTime = dateTime.atOffset(ZoneOffset.UTC).toLocalDateTime();
        Random random = new Random();

        long currentUnixTime = dateTime.toEpochSecond(ZoneOffset.UTC);
        long nextMonthUnixTime = countNextUnixTimeMonth(currentUnixTime);

        List<CDR> cdrList = new ArrayList<>();
        List<Long> subscribers = subscriberRepository.findAll().stream().map(Subscriber::getPhoneNumber).toList();
        for (int i = 1; i <= 12; i++) {
            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("cdr_files/cdr_" + i + ".txt", true)))) {
                while (currentUnixTime < nextMonthUnixTime) {

                    CDR cdr = CDR.builder()
                            .callType(random.nextInt(2) + 1)
                            .callerNumber(subscribers.get(random.nextInt(subscribers.size())))
                            .startTime(currentUnixTime)
                            .endTime(randCallTime(currentUnixTime))
                            .build();

                    cdrList.add(cdr);

                    out.println("""
                            0%d,%d,%d,%d
                            """.formatted(cdr.getCallType(), cdr.getCallerNumber(), cdr.getStartTime(), cdr.getEndTime()));
                    currentUnixTime = randNextCall(currentUnixTime, nextMonthUnixTime);
                }
                cdrRepository.saveAll(cdrList);
                currentUnixTime = nextMonthUnixTime;
                nextMonthUnixTime = countNextUnixTimeMonth(nextMonthUnixTime);

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
