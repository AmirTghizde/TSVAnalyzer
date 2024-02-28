package com.amirtghizde.tsvanalyzer.service;

import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
import org.springframework.web.multipart.MultipartFile;

public interface TSVFileService {

    TSVDetails readTsvFile(MultipartFile file);
    void saveTsvFile(MultipartFile file);

}
