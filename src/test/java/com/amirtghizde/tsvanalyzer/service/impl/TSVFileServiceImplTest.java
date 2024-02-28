package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.repository.TSVFileRepository;
import com.amirtghizde.tsvanalyzer.utils.exceptions.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TSVFileServiceImplTest {

    @Mock
    private TSVFileRepository tsvFileRepository;
    private TSVFileServiceImpl underTest;

    @BeforeEach
    void setUp() {
        underTest = new TSVFileServiceImpl(tsvFileRepository);
    }

    @Test
    void testReadTsvFile_Returns_FileStatics() {
        // Given
        String tsvContent = "Ali Alavi\t123456\t10.5\n" +
                            "Reza Alavi\t123456\t10.5\n" +
                            "Mohamad Alavi\t154387\t10.5\n" +
                            "Mohsen Alavi\t982346\t2.5\n" +
                            "AliReza Alavi\t182456\t15.2";

        MockMultipartFile file = new MockMultipartFile("test.tsv", tsvContent.getBytes());


        // When
        TSVDetails tsvDetails = underTest.readTsvFile(file);

        // Then
        assertThat(tsvDetails.getRecordCount()).isEqualTo(5);
        assertThat(tsvDetails.getTotalPrice()).isEqualTo(49.2);
        assertThat(tsvDetails.getDuplicateValues()).containsExactly("123456 10.5");

        verifyNoInteractions(tsvFileRepository);
    }

    @Test
    void testReadTsvFile_Catches_IOExceptions() throws IOException {
        // Given
        MockMultipartFile mockedFile = mock(MockMultipartFile.class);
        when(mockedFile.getInputStream()).thenThrow(new IOException("Mock message"));

        // When/Then
        assertThatThrownBy(() -> underTest.readTsvFile(mockedFile))
                .isInstanceOf(CustomException.class)
                .hasMessage("Error while reading TSV file: " + "Mock message" );
        verifyNoInteractions(tsvFileRepository);
    }

    @Test
    void testSaveTsvFile_NotSaves_UponError() throws IOException {

    }

    @Test
    void testGetDuplicateValues_Returns_DuplicateValuesList() {
        // Given
        Map<String, Integer> map = new HashMap<>();
        map.put("12345 12.6", 2);
        map.put("54321 10.2", 1);

        // When
        List<String> duplicateValues = underTest.getDuplicateValues(map);

        // Then
        assertThat(duplicateValues).contains("12345 12.6");
        assertThat(duplicateValues).doesNotContain("54321 10.2");
        verifyNoInteractions(tsvFileRepository);
    }

    @Test
    void testGetDuplicateValues_ReturnsEmpty_DuplicateValuesList() {
        // Given
        Map<String, Integer> map = new HashMap<>();
        map.put("12345 12.6", 1);
        map.put("54321 10.2", 1);

        // When
        List<String> duplicateValues = underTest.getDuplicateValues(map);

        // Then
        assertThat(duplicateValues).isEmpty();
        verifyNoInteractions(tsvFileRepository);
    }
}