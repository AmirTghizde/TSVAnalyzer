package com.amirtghizde.tsvanalyzer.controller;

import com.amirtghizde.tsvanalyzer.entity.TSVStatics;
import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class TSVController {
    private final TSVFileService tsvFileService;

    @Autowired
    public TSVController(TSVFileService tsvFileService) {
        this.tsvFileService = tsvFileService;
    }

    @PostMapping(value = "/viewStatics", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<List<String[]>> viewStatistics(@RequestParam("file") MultipartFile file) {
        System.out.println(file.getContentType());
        List<String[]> strings = tsvFileService.readTsvFile(file);
        return ResponseEntity.ok(strings);
    }

    @PostMapping(value = "/saveFile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> save(@RequestParam("file") MultipartFile file) {
        tsvFileService.saveTsvFile(file);
        return ResponseEntity.ok("✅ File added successfully ");
    }
}
