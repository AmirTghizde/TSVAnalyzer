package com.amirtghizde.tsvanalyzer.service;

import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import org.springframework.web.multipart.MultipartFile;

public interface TSVFileService {

    TSVDetails readTsvFile(MultipartFile file);
    void handleSave(MultipartFile file);
    void save(TSVFile tsvFile);

}
