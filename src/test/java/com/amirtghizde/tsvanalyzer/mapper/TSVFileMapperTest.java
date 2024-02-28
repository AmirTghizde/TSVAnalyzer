package com.amirtghizde.tsvanalyzer.mapper;

import com.amirtghizde.tsvanalyzer.entity.TSVFile;
import com.amirtghizde.tsvanalyzer.utils.exceptions.CustomException;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verifyNoInteractions;

class TSVFileMapperTest {
    @Test
    void testToEntity_ValidInput_ReturnsTSVFile() {
        // Given
        TSVFileMapper mapper = TSVFileMapper.INSTANCE;
        String[] input = {"Ali Alavi", "123456789", "10.5"};

        // When
        TSVFile result = mapper.toEntity(input);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Ali Alavi");
        assertThat(result.getAccountNumber()).isEqualTo("123456789");
        assertThat(result.getPrice()).isEqualTo(10.5);
    }
    @Test
    void testToEntity_NullInput_ThrowsException() {
        // Given
        TSVFileMapper mapper = TSVFileMapper.INSTANCE;
        String[] input = null;

        // When/Then
        assertThatThrownBy(() -> mapper.toEntity(input))
                .isInstanceOf(CustomException.class)
                .hasMessage("Invalid String array" );
    }

    @Test
    void testToEntity_InvalidInput_ThrowsException() {
        // Given
        TSVFileMapper mapper = TSVFileMapper.INSTANCE;
        String[] input = {"Ali Alavi", "10.5"};

        // When/Then
        assertThatThrownBy(() -> mapper.toEntity(input))
                .isInstanceOf(CustomException.class)
                .hasMessage("Invalid String array" );
    }

    @Test
    void testToEntity_InvalidPrice_ThrowsIllegalArgumentException() {
        // Given
        TSVFileMapper mapper = TSVFileMapper.INSTANCE;
        String[] input = {"Ali Alavi", "123456789", "not a number"};

        // When/Then
        assertThatThrownBy(() -> mapper.toEntity(input))
                .isInstanceOf(IllegalArgumentException.class);
    }
}