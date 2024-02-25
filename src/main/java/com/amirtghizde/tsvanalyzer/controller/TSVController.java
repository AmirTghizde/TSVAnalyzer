package com.amirtghizde.tsvanalyzer.controller;

import com.amirtghizde.tsvanalyzer.entity.TSVStatics;
import com.amirtghizde.tsvanalyzer.service.TSVFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v2")
public class TSVController {
    private final TSVFileService tsvFileService;

    @Autowired
    public TSVController(TSVFileService tsvFileService) {
        this.tsvFileService = tsvFileService;
    }

//    @PostMapping("/viewStatics")
//    public ResponseEntity<TSVStatics> viewStatistics(@Valid @RequestBody RegisterDto registerDto) {
//        Customer customer = customerService.register(registerDto);
//        CustomerDataDto customerDto = UserMapper.INSTANCE.toCustomerDataDto(customer);
//        return ResponseEntity.status(HttpStatus.CREATED).body(customerDto);
//    }
}
