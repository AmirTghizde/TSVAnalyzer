package com.amirtghizde.tsvanalyzer.controller;

import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Objects;

@RestController
@RequestMapping("/v1/api")
public class TSVController {
    private final TSVFileService tsvFileService;

    @Autowired
    public TSVController(TSVFileService tsvFileService) {
        this.tsvFileService = tsvFileService;
    }

    @PostMapping(value = "/viewStatics", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<TSVDetails> viewStatistics(@RequestParam("file") MultipartFile file) {
        if (!Objects.equals(file.getContentType(), "text/tab-separated-values")) {
            throw new IllegalArgumentException("Only supported format is TSV");
        }
        TSVDetails tsvDetails = tsvFileService.readTsvFile(file);
        return ResponseEntity.ok(tsvDetails);
    }

    @PostMapping(value = "/saveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> save(@RequestParam("file") MultipartFile file) {
        tsvFileService.handleSave(file);
        return ResponseEntity.ok("✅ File added successfully ");
    }
}
