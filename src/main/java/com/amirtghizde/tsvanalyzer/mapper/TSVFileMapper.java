package com.amirtghizde.tsvanalyzer.mapper;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import org.apache.commons.csv.CSVRecord;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TSVFileMapper {

    TSVFileMapper INSTANCE = Mappers.getMapper(TSVFileMapper.class);

    TSVFile toEntity(CSVRecord record);

}
