package com.nexign.bootcamp.emulator.service;

import com.nexign.bootcamp.emulator.model.CDR;
import com.nexign.bootcamp.emulator.model.CallType;
import com.nexign.bootcamp.emulator.model.Subscriber;
import com.nexign.bootcamp.emulator.repository.CDRRepository;
import com.nexign.bootcamp.emulator.repository.SubscriberRepository;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.apache.commons.io.file.PathUtils.deleteDirectory;

/**
 * Service for emulating Call Detail Records (CDR) data and persisting it to files and database.
 */
@Service
public class CDRService {

    /**
     * Repository for managing CDR entities.
     */
    @Resource
    private CDRRepository cdrRepository;

    /**
     * Repository for managing subscriber entities.
     */
    @Resource
    private SubscriberRepository subscriberRepository;

    /**
     * Logger for the CDRService class.
     */
    Logger log = LoggerFactory.getLogger(CDRService.class);

    /**
     * Directory name where CDR files are saved.
     */
    @Value("${directory.cdr.name}")
    private String dirName;

    /**
     * Calculates the Unix time for the start of the next month.
     *
     * @param currentUnixTime Unix time for the current month
     * @return Unix time for the start of the next month
     */
    private long countNextUnixTimeMonth (long currentUnixTime) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochSecond(currentUnixTime), ZoneOffset.UTC);
        LocalDateTime newDateTime = dateTime.plusMonths(1);

        return newDateTime.toEpochSecond(ZoneOffset.UTC);
    }

    /**
     * Generates a random Unix time for the next call within the same month.
     *
     * @param prevCallUnixTime   Unix time for the previous call
     * @param limitMonthUnixTime Unix time limit for the current month
     * @return random Unix time for the next call
     */
    private long randNextCall (long prevCallUnixTime, long limitMonthUnixTime) {
        int min = 28800, max = 57600;
        Random random = new Random();
        long newCallUnixTime = prevCallUnixTime + random.nextInt(max - min) + min;

        return Math.min(newCallUnixTime, limitMonthUnixTime);
    }

    /**
     * Generates a random call time based on the start call Unix time.
     *
     * @param startCallUnixTime Unix time for the start of the call
     * @return random call end time
     */
    private long randCallTime (long startCallUnixTime) {
        Random random = new Random();
        return startCallUnixTime + random.nextInt(1800);
    }

    /**
     * Generates a random call type (incoming or outcoming).
     *
     * @return random call type
     */
    private CallType randCallType () {
        Random random = new Random();
         if (0 == random.nextInt(2)) {
             return CallType.INCOMING;
         } else {
             return CallType.OUTCOMING;
         }
    }

    /**
     * Converts a call type enum to its corresponding integer representation.
     *
     * @param callType call type enum
     * @return integer representation of the call type (1 for incoming, 2 for outgoing)
     */
    private int callTypeToInt (CallType callType) {
        return CallType.INCOMING.equals(callType) ? 1 : 2;
    }

    /**
     * Emulates the switching system by generating and persisting mock CDR data.
     */
    @Transactional
    public void emulateSwitch () {
        try {
            if (!Files.exists(Paths.get(dirName))) {
                Files.createDirectory(Paths.get(dirName));
            } else {
                deleteDirectory(Paths.get(dirName));
                Files.createDirectory(Paths.get(dirName));
            }
        } catch (IOException e) {
            log.error("Fail to create directory: " + dirName);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy HH:mm:ss");
        LocalDateTime dateTime = LocalDateTime.parse("01/01/2023 00:00:00", formatter);
        dateTime = dateTime.atOffset(ZoneOffset.UTC).toLocalDateTime();
        Random random = new Random();

        long currentUnixTime = dateTime.toEpochSecond(ZoneOffset.UTC);
        long nextMonthUnixTime = countNextUnixTimeMonth(currentUnixTime);

        List<CDR> cdrList = new ArrayList<>();
        List<Long> subscribers = subscriberRepository.findAll().stream().map(Subscriber::getPhoneNumber).toList();
        for (int i = 1; i <= 12; i++) {
            String pathFileString = dirName + "/cdr_" + i + "_2023.txt";
            Path pathFile = Paths.get(pathFileString);

            try {
                if (!Files.exists(pathFile)) Files.createFile(pathFile);
            } catch (IOException e) {
                log.error("Fail to create file: " + pathFile);
            }

            try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(pathFileString, true)))) {
                while (currentUnixTime < nextMonthUnixTime) {

                    CDR cdr = CDR.builder()
                            .callType(randCallType())
                            .callerNumber(subscribers.get(random.nextInt(subscribers.size())))
                            .startTime(currentUnixTime)
                            .endTime(randCallTime(currentUnixTime))
                            .build();

                    cdrList.add(cdr);

                    out.println("""
                            0%d,%d,%d,%d
                            """.formatted(
                                    callTypeToInt(cdr.getCallType()),
                                    cdr.getCallerNumber(),
                                    cdr.getStartTime(),
                                    cdr.getEndTime()));
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
