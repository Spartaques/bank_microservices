package com.andriibashuk.loanservice.entity;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class BalanceDataConverter implements AttributeConverter<Map<String, Integer>, String> {
    private final ObjectMapper mapper;

    public BalanceDataConverter() {
        this.mapper = new ObjectMapper();
    }
    @Override
    public String convertToDatabaseColumn(Map<String, Integer> attribute) {
        try {
            return mapper.writeValueAsString(attribute);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Map<String, Integer> convertToEntityAttribute(String dbData) {
        try {
            return mapper.readValue(dbData,new TypeReference<HashMap<String, Integer>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
