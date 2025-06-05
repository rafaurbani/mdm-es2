package com.rafaurbani.dem.service;

import com.rafaurbani.dem.client.MdmServiceProxy;
import com.rafaurbani.dem.dto.MasterDataCountryDTO;
import com.rafaurbani.dem.exception.MdmServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class LoadingService {

    private static final Logger logger = LoggerFactory.getLogger(LoadingService.class);
    private final MdmServiceProxy mdmServiceProxy;

    public LoadingService(MdmServiceProxy mdmServiceProxy) {
        this.mdmServiceProxy = mdmServiceProxy;
    }

    public void loadDataToMdm(String transactionId, List<MasterDataCountryDTO> transformedData) {
        if (transformedData == null || transformedData.isEmpty()) {
            logger.warn("Transaction [{}]: No data transformed to load on MDM.", transactionId);
            return;
        }

        try {
            logger.info("Transaction [{}]: Starting loading of {} entries for the MDM Service.", transactionId, transformedData.size());
            mdmServiceProxy.loadCountriesToMdm(transformedData);
            logger.info("Transaction [{}]: Successfully sent data to MDM Service.", transactionId);
        } catch (MdmServiceUnavailableException e) {
            logger.error("Transaction [{}]: Error on loading data to MDM: {}", transactionId, e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Transaction [{}]: Error on loading to MDM: {}", transactionId, e.getMessage(), e);
            throw new RuntimeException("Error on loading to MDM: " + e.getMessage(), e);
        }
    }
}