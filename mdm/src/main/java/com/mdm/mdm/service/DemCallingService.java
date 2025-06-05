package com.mdm.mdm.service;

import com.mdm.mdm.client.DemServiceProxy;
import com.mdm.mdm.dto.MasterDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemCallingService {

    private static final Logger logger = LoggerFactory.getLogger(DemCallingService.class);
    private final DemServiceProxy demServiceProxy;

    public DemCallingService(DemServiceProxy demServiceProxy) {
        this.demServiceProxy = demServiceProxy;
    }

    public List<MasterDataDTO> startEtl() {
        List<MasterDataDTO> data = null;

        try {
            data = demServiceProxy.startEtl();
        } catch (Exception e) {
            logger.error("Error on loading to MDM: {}", e.getMessage(), e);
            throw new RuntimeException("Error on loading to MDM: " + e.getMessage(), e);
        }

        return data;
    }
}