package com.syrtin.banking.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

public class JacksonObjectMapper extends ObjectMapper {

    private static final ObjectMapper MAPPER = new JacksonObjectMapper();

    private JacksonObjectMapper() {
        registerModule(new JavaTimeModule());
    }

    public static ObjectMapper getMapper() {
        return MAPPER;
    }
}