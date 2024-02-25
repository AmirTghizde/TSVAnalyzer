package com.amirtghizde.tsvanalyzer.repository;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TSVFileRepository extends JpaRepository<TSVFile, Long> {

}
