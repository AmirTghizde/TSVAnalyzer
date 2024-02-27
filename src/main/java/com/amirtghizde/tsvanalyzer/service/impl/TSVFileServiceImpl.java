package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.entity.TSVStatics;
import com.amirtghizde.tsvanalyzer.mapper.TSVFileMapper;
import com.amirtghizde.tsvanalyzer.repository.TSVFileRepository;
import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import com.amirtghizde.tsvanalyzer.utils.exceptions.CustomException;
import com.amirtghizde.tsvanalyzer.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TSVFileServiceImpl implements TSVFileService {

    private final TSVFileRepository tsvFileRepository;

    @Autowired
    public TSVFileServiceImpl(TSVFileRepository tsvFileRepository) {
        this.tsvFileRepository = tsvFileRepository;
    }

    @Override
    public TSVStatics readTsvFile(MultipartFile file) {

        List<String[]> tsvData = new ArrayList<>();
        Map<String, Integer> duplicateCountMap = new HashMap<>();
        double totalAmount = 0;


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                tsvData.add(data);

                double price = Double.parseDouble(data[2]);
                totalAmount += price;

                StringBuilder builder = new StringBuilder(data[1]).append(" ").append(data[2]);
                duplicateCountMap.put(builder.toString(), duplicateCountMap.getOrDefault(builder.toString(), 0) + 1);
            }
        } catch (IOException e) {
            throw new CustomException("Error while reading TSV file: " + e.getMessage());
        }

        int totalRecords = tsvData.size();
        List<String> duplicateRecords = getDuplicateValues(duplicateCountMap);

        return new TSVStatics(totalRecords, totalAmount, duplicateRecords);
    }

    @Override
    @Transactional
    public void saveTsvFile(MultipartFile file) {
        Map<String, Integer> duplicateCountMap = new HashMap<>();

        try (InputStream inputStream = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");

                StringBuilder builder = new StringBuilder(data[1]).append(" ").append(data[2]);
                String record = builder.toString();

                if (duplicateCountMap.containsKey(builder.toString())) {
                    throw new DuplicateValueException("Duplicate value found: " + record);
                }

                duplicateCountMap.put(record, 1);
                TSVFile tsvFile = TSVFileMapper.INSTANCE.toEntity(data);
                try {
                    tsvFileRepository.save(tsvFile);
                } catch (PersistenceException e) {
                    throw new CustomException(e.getMessage());
                }
            }
        } catch (IOException e) {
            throw new CustomException(e.getMessage());
        }
    }

    protected List<String> getDuplicateValues(Map<String, Integer> duplicateCountMap) {
        List<String> duplicateRecords = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : duplicateCountMap.entrySet()) {
            if (entry.getValue() > 1) {
                duplicateRecords.add(entry.getKey());
            }
        }
        return duplicateRecords;
    }
}
