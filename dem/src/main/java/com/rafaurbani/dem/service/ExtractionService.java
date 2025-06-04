package com.rafaurbani.dem.service;

import com.rafaurbani.dem.dto.CountryRawDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ExtractionService {
    private static final Logger logger = LoggerFactory.getLogger(EtlTransactionService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public List<CountryRawDTO> fetchRawCountryData() {
        String apiUrl = "https://restcountries.com/v3.1/all";
        try {
            ResponseEntity<CountryRawDTO[]> response = restTemplate.getForEntity(apiUrl, CountryRawDTO[].class);
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            logger.warn(e.getMessage());
            return Collections.emptyList();
        }
    }
}