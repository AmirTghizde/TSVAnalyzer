package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.entity.TSVStatics;
import com.amirtghizde.tsvanalyzer.mapper.TSVFileMapper;
import com.amirtghizde.tsvanalyzer.repository.TSVFileRepository;
import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import com.amirtghizde.tsvanalyzer.utils.exceptions.DuplicateValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TSVFileServiceImpl implements TSVFileService {

    private TSVFileRepository tsvFileRepository;

    @Autowired
    public TSVFileServiceImpl(TSVFileRepository tsvFileRepository) {
        this.tsvFileRepository = tsvFileRepository;
    }

    @Override
    public TSVStatics readTsvFile(MultipartFile file) {

        List<String[]> tsvData = new ArrayList<>();
        Map<String, Integer> duplicateCountMap = new HashMap<>();
        double totalAmount = 0;


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                tsvData.add(data);

                double price = Double.parseDouble(data[2]);
                totalAmount += price;

                StringBuilder stringBuilder = new StringBuilder();
                StringBuilder builder = stringBuilder.append(data[1]).append(" ").append(data[2]);
                duplicateCountMap.put(builder.toString(), duplicateCountMap.getOrDefault(builder.toString(), 0) + 1);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading TSV file: " + e.getMessage(), e);
        }

        int totalRecords = tsvData.size();
        List<String> duplicateRecords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : duplicateCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicateRecords.add(entry.getKey());
            }
        }

        return new TSVStatics(totalRecords, totalAmount, duplicateRecords);
    }


    @Override
    public void saveTsvFile(MultipartFile file) {
        Map<String, Integer> duplicateCountMap = new HashMap<>();

        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");


                StringBuilder accountNumberBuilder = new StringBuilder();
                accountNumberBuilder.append(data[1]).append(data[2]);
                String accountNumber = accountNumberBuilder.toString();

                if (duplicateCountMap.containsKey(accountNumber)) {
                    throw new DuplicateValueException("Duplicate value found: " + accountNumber);
                }

                duplicateCountMap.put(accountNumber, 1);
                TSVFile tsvFile = TSVFileMapper.INSTANCE.toEntity(data);
                tsvFileRepository.save(tsvFile);

            }
        } catch (IOException e) {
            // Handle any exceptions
            e.printStackTrace();
        }
    }
}
