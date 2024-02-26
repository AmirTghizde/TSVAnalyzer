package com.amirtghizde.tsvanalyzer.mapper;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TSVFileMapper {
    TSVFileMapper INSTANCE = Mappers.getMapper(TSVFileMapper.class);

    default TSVFile toEntity(String[] strings) {
        if (strings == null || strings.length < 3) {
            return null;
        }

        TSVFile tsvFile = new TSVFile();
        tsvFile.setName(strings[0]);
        tsvFile.setAccountNumber(strings[1]);
        tsvFile.setPrice(Double.parseDouble(strings[2]));

        return tsvFile;
    }

}

