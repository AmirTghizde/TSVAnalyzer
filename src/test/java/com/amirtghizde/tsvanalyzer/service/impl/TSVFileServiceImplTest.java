package com.amirtghizde.tsvanalyzer.service.impl;

import com.amirtghizde.tsvanalyzer.entity.TSVDetails;
import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.repository.TSVFileRepository;
import com.amirtghizde.tsvanalyzer.utils.exceptions.CustomException;
import com.amirtghizde.tsvanalyzer.utils.exceptions.DuplicateValueException;
import jakarta.persistence.PersistenceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
    void testReadTsvFile() {
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
        String errorMessage="Mock message";

        MockMultipartFile mockedFile = mock(MockMultipartFile.class);
        when(mockedFile.getInputStream()).thenThrow(new IOException(errorMessage));

        // When/Then
        assertThatThrownBy(() -> underTest.readTsvFile(mockedFile))
                .isInstanceOf(CustomException.class)
                .hasMessage("Error while reading TSV file: " + errorMessage );
        verifyNoInteractions(tsvFileRepository);
    }

    @Test
    void testHandleSave() throws IOException {
        // Given
        String fileContent = "data1\taccountNumber1\t10.5\n" +
                             "data2\taccountNumber2\t12.2";

        InputStream inputStream = new ByteArrayInputStream(fileContent.getBytes());
        MockMultipartFile file = new MockMultipartFile("file", "test.tsv",
                "text/plain", inputStream);

        // Act
        underTest.handleSave(file);

        // Assert
        verify(tsvFileRepository, times(2)).save(any(TSVFile.class));
    }

    @Test
    void testHandleSave_Catches_IOException() throws IOException {
        // Given
        String errorMessage="Mock message";

        MockMultipartFile mockedFile = mock(MockMultipartFile.class);
        when(mockedFile.getInputStream()).thenThrow(new IOException(errorMessage));

        // When/Then
        assertThatThrownBy(() -> underTest.handleSave(mockedFile))
                .isInstanceOf(CustomException.class)
                .hasMessage("Error while reading TSV file: " + errorMessage );
        verifyNoInteractions(tsvFileRepository);
    }

    @Test
    void testSave_savesFile() {
        // Given
        TSVFile tsvFile = new TSVFile();

        // When
        underTest.save(tsvFile);

        // Then
        verify(tsvFileRepository, times(1)).save(tsvFile);
        verifyNoMoreInteractions(tsvFileRepository);
    }

    @Test
    void testSave_Catches_PersistenceException() {
        // Given
        String errorMessage = "Persistence message";

        TSVFile tsvFile = new TSVFile();
        when(tsvFileRepository.save(tsvFile)).thenThrow(new PersistenceException(errorMessage));

        // When/Then
        assertThatThrownBy(() -> underTest.save(tsvFile))
                .isInstanceOf(CustomException.class)
                .hasMessage("PersistenceException occurred: " + errorMessage);
        verifyNoMoreInteractions(tsvFileRepository);
    }

    @Test
    void testCheckDuplication_DoesNothing() {
        // Given
        String[] data = {"Ali alavi", "accountNumber", "price"};
        Map<String, Integer> duplicateCountMap = new HashMap<>();

        // When
        underTest.checkDuplication(data, duplicateCountMap);

        // Then
        String key = "accountNumber price";
        assertThat(duplicateCountMap.get(key)).isEqualTo(1);
    }

    @Test
    void testCheckDuplication_throwsDuplicateValueException() {
        // Given
        String[] data = {"Ali alavi", "accountNumber", "price"};
        Map<String, Integer> duplicateCountMap = new HashMap<>();

        String key = "accountNumber price";
        duplicateCountMap.put(key, 1);

        // When/Then
        assertThatThrownBy(() -> underTest.checkDuplication(data, duplicateCountMap))
                .isInstanceOf(DuplicateValueException.class)
                .hasMessage("Duplicate value found: " + key);
    }

    @Test
    void testUpdateDuplicateRecords_Returns_DuplicateList() {
        // Given
        String[] data = {"Ali alavi", "accountNumber", "price"};
        Map<String, Integer> duplicateCountMap = new HashMap<>();
        List<String> duplicateRecords = new LinkedList<>();

        String key = "accountNumber price";
        duplicateCountMap.put(key, 1);

        // When
        underTest.updateDuplicateRecords(data, duplicateCountMap, duplicateRecords);

        // Then
        assertThat(duplicateRecords).containsExactly("accountNumber price");
    }

    @Test
    void testUpdateDuplicateRecords_ReturnsEmpty_DuplicateList() {
        // Given
        String[] data = {"Ali alavi", "accountNumber", "price"};
        Map<String, Integer> duplicateCountMap = new HashMap<>();
        List<String> duplicateRecords = new LinkedList<>();

        // When
        underTest.updateDuplicateRecords(data, duplicateCountMap, duplicateRecords);

        // Then
        assertThat(duplicateRecords).isEmpty();
    }
}