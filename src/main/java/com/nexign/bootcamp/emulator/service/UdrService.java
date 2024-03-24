package com.nexign.bootcamp.emulator.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nexign.bootcamp.emulator.component.TimeMeterSerializer;
import com.nexign.bootcamp.emulator.model.CDR;
import com.nexign.bootcamp.emulator.model.CallType;
import com.nexign.bootcamp.emulator.model.TimeMeter;
import com.nexign.bootcamp.emulator.model.Udr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UdrService {

    Logger log = LoggerFactory.getLogger(UdrService.class);

    @Value("${directory.cdr.name}")
    private String dirCDRName;

    @Value("${directory.report.name}")
    private String dirReportName;

    private List<CDR> readCDRFiles(int index) {
        String pathFile = dirCDRName + "/cdr_" + index + "_2023.txt";
        return readFileCDRToListCDR(pathFile);
    }

    public void generateReport () {
        List<CDR> cdrList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            cdrList.addAll(readCDRFiles(i));
        }

        List<Udr> udrList = groupedUdrs(cdrList).values().stream().toList();
        udrToJson(dirReportName + "/annual_report_2023.json", udrList);
    }

    public void generateReport (Long msisdn) {
        List<CDR> cdrList = new ArrayList<>();
        for (int i = 1; i <= 12; i++) {
            cdrList.addAll(readCDRFiles(i));
        }
        groupedUdrs(cdrList).values().stream()
                .filter(udr -> udr.getMsisdn().equals(msisdn)).findFirst()
                .ifPresent(udr ->
                        udrToJson("%s/annual_report_%d_2023.json".formatted(dirReportName, msisdn), List.of(udr)));

    }

    public void generateReport (Long msisdn, int month) {
        List<CDR> cdrList = new ArrayList<>(readCDRFiles(month));

        groupedUdrs(cdrList).values().stream()
                .filter(udr -> udr.getMsisdn().equals(msisdn)).findFirst()
                .ifPresent(udr ->
                        udrToJson("%s/month_%d_report_%d_2023.json".formatted(dirReportName, month, msisdn), List.of(udr)));
    }

    private Map<Long, Udr> groupedUdrs (List<CDR> cdrList) {
        return cdrList.stream()
            .collect(Collectors.toMap(
                    CDR::getCallerNumber,
                            cdr -> Udr.builder()
                                    .msisdn(cdr.getCallerNumber())
                                    .incomingCall(cdr.getCallType().equals(CallType.INCOMING)
                                            ? new TimeMeter(cdr.getDuration())
                                            : new TimeMeter(0L))
                                    .outcomingCall(cdr.getCallType().equals(CallType.OUTCOMING)
                                            ? new TimeMeter(cdr.getDuration())
                                            : new TimeMeter(0L))
                                    .build(),
                    (Udr udr1, Udr udr2) -> Udr.builder()
                            .msisdn(udr1.getMsisdn())
                            .incomingCall(udr1.getIncomingCall().merge(udr2.getIncomingCall()))
                            .outcomingCall(udr1.getOutcomingCall().merge(udr2.getOutcomingCall()))
                            .build()));
    }

    private void udrToJson (String pathOfFile, List<Udr> udrList) {
        Path pathDirectory = Paths.get(dirReportName);
        Path pathFile = Paths.get(pathOfFile);

        try {
            if (!Files.exists(pathDirectory)) {
                Files.createDirectory(pathDirectory);
            }
            if (!Files.exists(pathFile)) {
                Files.createFile(pathFile);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(TimeMeter.class, new TimeMeterSerializer())
                .create();

        String json = gson.toJson(udrList);

        try (BufferedWriter br = new BufferedWriter(new FileWriter(pathOfFile))) {
            br.write(json);
        } catch (IOException e) {
            log.error("Error when writing Json to a file: " + pathOfFile);
        }
    }

    private CallType stringToCallType (String callTypeString) {
        return callTypeString.equals("01") ? CallType.INCOMING : CallType.OUTCOMING;
    }
    public List<CDR> readFileCDRToListCDR (String fileName) {
        List<CDR> listCDR = new ArrayList<>();

        if (Files.exists(Paths.get(fileName))) {
            try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (line.trim().isEmpty())
                        continue;

                    String[] items = line.split(",");
                    CDR cdr = CDR.builder()
                            .callType(stringToCallType(items[0]))
                            .callerNumber(Long.parseLong(items[1]))
                            .startTime(Long.parseLong(items[2]))
                            .endTime(Long.parseLong(items[3]))
                            .build();
                    listCDR.add(cdr);
                }
            } catch (IOException e) {
                log.error("Fail when reading a file: " + fileName);
            }
        }

        return listCDR;
    }
}
