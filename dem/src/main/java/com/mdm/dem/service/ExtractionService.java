package com.mdm.dem.service;

import com.mdm.dem.dto.MasterDataRawDTO;
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
    private static final Logger logger = LoggerFactory.getLogger(ExtractionService.class);
    private final RestTemplate restTemplate = new RestTemplate();

    public List<MasterDataRawDTO> fetchRawData(String source) {
        try {
            ResponseEntity<MasterDataRawDTO[]> response = restTemplate.getForEntity(source, MasterDataRawDTO[].class);
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