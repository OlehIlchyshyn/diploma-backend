package com.nulp.fetchproductdata.common.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.persistence.AttributeConverter;
import java.io.IOException;
import java.util.Map;

//@Component
@Slf4j
public class HashMapConverter implements AttributeConverter<Map<String, Object>, String> {

    private ObjectMapper objectMapper;

    @Override
    public String convertToDatabaseColumn(Map<String, Object> additionalProperties) {

        String additionalPropertiesJson = null;
        try {
            additionalPropertiesJson = objectMapper.writeValueAsString(additionalProperties);
        } catch (final JsonProcessingException e) {
            log.error("JSON writing error", e);
        }

        return additionalPropertiesJson;
    }

    @Override
    public Map<String, Object> convertToEntityAttribute(String additionalPropertiesJson) {

        Map<String, Object> additionalProperties = null;
        try {
            additionalProperties = objectMapper.readValue(additionalPropertiesJson, Map.class);
        } catch (final IOException e) {
            log.error("JSON reading error", e);
        }

        return additionalProperties;
    }

}