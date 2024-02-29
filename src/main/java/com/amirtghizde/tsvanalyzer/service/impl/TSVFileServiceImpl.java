package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
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
import java.util.*;

@Service
public class TSVFileServiceImpl implements TSVFileService {

    private final TSVFileRepository tsvFileRepository;

    @Autowired
    public TSVFileServiceImpl(TSVFileRepository tsvFileRepository) {
        this.tsvFileRepository = tsvFileRepository;
    }

    @Override
    public TSVDetails readTsvFile(MultipartFile file) {

        List<String[]> tsvData = new ArrayList<>();
        List<String> duplicateRecords = new LinkedList<>();
        Map<String, Integer> duplicateCountMap = new HashMap<>();
        double totalAmount = 0;


        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                tsvData.add(data);

                double price = Double.parseDouble(data[2]);
                totalAmount += price;

                updateDuplicateRecords(data, duplicateCountMap, duplicateRecords);

            }
        } catch (IOException e) {
            throw new CustomException("Error while reading TSV file: " + e.getMessage());
        }

        int totalRecords = tsvData.size();

        return new TSVDetails(totalRecords, totalAmount, duplicateRecords);
    }

    @Override
    @Transactional
    public void handleSave(MultipartFile file) {
        Map<String, Integer> duplicateCountMap = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {

            String line;

            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");

                checkDuplication(data, duplicateCountMap);

                TSVFile tsvFile = TSVFileMapper.INSTANCE.toEntity(data);

                save(tsvFile);
            }

        } catch (IOException e) {
            throw new CustomException("Error while reading TSV file: " + e.getMessage());
        }
    }

    @Override
    public void save(TSVFile tsvFile) {
        try {
            tsvFileRepository.save(tsvFile);
        } catch (PersistenceException e) {
            throw new CustomException("PersistenceException occurred: " + e.getMessage());
        }
    }

    protected void checkDuplication(String[] data, Map<String, Integer> duplicateCountMap) {

        /* Makes a key with the values of accountNumber & price
        * checks if the key already exists if yes throws exception
        * if no adds it to the map */

        StringBuilder builder = new StringBuilder(data[1]).append(" ").append(data[2]);
        String record = builder.toString();

        if (duplicateCountMap.containsKey(record)) {
            throw new DuplicateValueException("Duplicate value found: " + record);
        }

        duplicateCountMap.put(record, 1);
    }

    protected void updateDuplicateRecords(String[] data, Map<String, Integer> duplicateCountMap,
                                        List<String> duplicateRecords) {

        /* Makes a key with the values of accountNumber & price
         * puts the key in the map and increments the count
         * checks if there is records with value equal to one (meaning dupe values)
         * if yes adds the record to duplicateRecords list */

        StringBuilder builder = new StringBuilder(data[1]).append(" ").append(data[2]);
        Integer count = duplicateCountMap.put(builder.toString(), duplicateCountMap.getOrDefault(builder.toString(), 0) + 1);

        if (count != null && count == 1) {
            duplicateRecords.add(builder.toString());
        }

    }
}
