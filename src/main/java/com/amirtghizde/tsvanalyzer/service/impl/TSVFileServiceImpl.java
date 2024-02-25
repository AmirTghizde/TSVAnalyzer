package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class TSVFileServiceImpl implements TSVFileService {


    @Override
    public List<String[]> readTsvFile(MultipartFile file) {

        List<String[]> tsvData = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                tsvData.add(data);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error reading TSV file: " + e.getMessage(), e);
        }

        return tsvData;
    }


    @Override
    public void saveTsvFile(MultipartFile file) {

    }
}
