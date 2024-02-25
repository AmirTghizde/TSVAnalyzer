package com.amirtghizde.tsvanalyzer.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TSVFileService {

    List<String[]> readTsvFile(MultipartFile file);
    void saveTsvFile(MultipartFile file);

}
