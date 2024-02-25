package com.amirtghizde.tsvanalyzer.service;

import com.amirtghizde.tsvanalyzer.entity.TSVStatics;

public interface TSVFileService {

    TSVStatics readTsvFile(String path);
    void saveTsvFile(String path);

}
