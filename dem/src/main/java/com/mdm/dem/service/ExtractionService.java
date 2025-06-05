package com.mdm.dem.service;

import com.mdm.dem.dto.RawMasterDataDTO;
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
    private final RestTemplate restTemplate = new RestTemplate();

    public List<RawMasterDataDTO> fetchRawData(String source) {
        try {
            ResponseEntity<RawMasterDataDTO[]> response = restTemplate.getForEntity(source, RawMasterDataDTO[].class);
            if (response.getBody() != null) {
                return Arrays.asList(response.getBody());
            }
            return Collections.emptyList();
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }
}