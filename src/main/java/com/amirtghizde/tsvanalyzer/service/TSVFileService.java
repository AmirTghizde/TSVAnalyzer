package com.amirtghizde.tsvanalyzer.service;

import com.amirtghizde.tsvanalyzer.entity.TSVStatics;
import org.springframework.web.multipart.MultipartFile;

public interface TSVFileService {

    TSVStatics readTsvFile(MultipartFile file);
    void saveTsvFile(MultipartFile file);

}
