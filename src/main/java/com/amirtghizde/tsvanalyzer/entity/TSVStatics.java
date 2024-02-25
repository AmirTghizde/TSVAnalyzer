package com.amirtghizde.tsvanalyzer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TSVStatics {

    private int recordCount;
    private double totalPrice;
    private List<String> duplicateValues;


}
