package com.mdm.mdm.service;

import com.mdm.dem.client.MdmServiceProxy;
import com.mdm.dem.dto.MasterDataTransfomedDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadingService {

    private static final Logger logger = LoggerFactory.getLogger(LoadingService.class);
    private final DemServiceProxy mdmServiceProxy;

    public LoadingService(MdmServiceProxy mdmServiceProxy) {
        this.mdmServiceProxy = mdmServiceProxy;
    }

    public void loadDataToMdm(String transactionId, List<MasterDataTransfomedDTO> transformedData) {
        if (transformedData == null || transformedData.isEmpty()) {
            logger.warn("Transaction [{}]: No data transformed to load on MDM.", transactionId);
            return;
        }

        try {
            logger.info("Transaction [{}]: Starting loading of {} entries for the MDM Service.", transactionId, transformedData.size());
            mdmServiceProxy.loadCountriesToMdm(transformedData);
            logger.info("Transaction [{}]: Successfully sent data to MDM Service.", transactionId);
        } catch (Exception e) {
            logger.error("Transaction [{}]: Error on loading to MDM: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Error on loading to MDM: " + e.getMessage(), e);
        }
    }
}