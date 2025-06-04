package com.rafaurbani.dem.service;

import com.rafaurbani.dem.dto.CountryRawDTO;
import com.rafaurbani.dem.dto.EtlTransactionDTO;
import com.rafaurbani.dem.dto.MasterDataCountryDTO;
import com.rafaurbani.dem.exception.MdmServiceUnavailableException;
import com.rafaurbani.dem.exception.ResourceNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class EtlTransactionService {
    private static final Logger logger = LoggerFactory.getLogger(EtlTransactionService.class);
    private final Map<String, EtlTransactionDTO> transactions = new HashMap<>();

    private final ExtractionService extractionService;
    private final TransformationService transformationService;
    private final LoadingService loadingService;

    public EtlTransactionService(ExtractionService extractionService,
                                 TransformationService transformationService,
                                 LoadingService loadingService) {
        this.extractionService = extractionService;
        this.transformationService = transformationService;
        this.loadingService = loadingService;
    }

    public EtlTransactionDTO initiateEtl(String source) {
        EtlTransactionDTO transaction = new EtlTransactionDTO();
        transaction.setSourceDetails(source);
        transactions.put(transaction.getTransactionId(), transaction);
        logger.info("Starting ETL transaction with source: {}", source);

        try {
            // extraction
            logger.info("Transaction [{}]: Starting extraction phase.", transaction.getTransactionId());
            List<CountryRawDTO> rawData = extractionService.fetchRawCountryData();
            transaction.setExtractedRecordsCount(rawData.size());
            logger.info("Transaction [{}]: Extraction completed with {} records.", transaction.getTransactionId(), rawData.size());

            if (rawData.isEmpty() && transaction.getExtractedRecordsCount() == 0) {
                logger.warn("Transaction [{}]: No data extracted from source. Aborting ETL process.", transaction.getTransactionId());
                transaction.setStatus("FAILED");
                return transaction;
            }

            // transformation
            transaction.setStatus("TRANSFORMING");
            logger.info("Transaction [{}]: Starting transformation phase.", transaction.getTransactionId());
            List<MasterDataCountryDTO> transformedData = transformationService.transformCountryData(rawData);
            transaction.setTransformedRecordsCount(transformedData.size());
            logger.info("Transaction [{}]: Transformation completed with {} records.", transaction.getTransactionId(), transformedData.size());
            transaction.setTransformedData(transformedData);

            transaction.setStatus("READY FOR LOADING");
            logger.info("Transaction [{}]: Ready to loading phase", transaction.getTransactionId());
        } catch (Exception e) {
            logger.error("Transaction [{}]: Unexpected error during ETL process: {}", transaction.getTransactionId(), e.getMessage(), e);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage(e.getMessage());
            transaction.setEndTime(LocalDateTime.now());
        }

        return mapToDto(transaction);
    }

    public EtlTransactionDTO loadToMdm(String transactionId) {
        EtlTransactionDTO transaction = transactions.get(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException(transactionId);
        }

        if (!"READY FOR LOADING".equals(transaction.getStatus())) {
            logger.warn("Transaction [{}]: Not ready for loading. Current status: {}", transactionId, transaction.getStatus());
            throw new IllegalStateException("Transaction [" + transactionId + "]: Not ready for loading.");
        }

        if (transaction.getTransformedData() == null || transaction.getTransformedData().isEmpty()) {
            logger.warn("Transaction [{}]: No transformed data available for loading.", transactionId);
            transaction.setStatus("DONE WITHOUT DATA");
            transaction.setErrorMessage("No transformed data available for loading.");
            transaction.setEndTime(LocalDateTime.now());
            return mapToDto(transaction);
        }

        try {
            transaction.setStatus("LOADING");
            logger.info("Transaction [{}]: Starting loading.", transactionId);
            loadingService.loadDataToMdm(transactionId, transaction.getTransformedData());
            transaction.setLoadedRecordsCount(transaction.getTransformedData().size());
            transaction.setStatus("DONE");
            logger.info("Transaction [{}]: Loading completed successfully.", transactionId);
        } catch (MdmServiceUnavailableException e) {
            logger.error("Transaction [{}]: Error loading data to MDM.", transactionId, e);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage(e.getMessage());
        } catch (Exception e) {
            logger.error("Transaction [{}]: Unexpected error during loading.", transactionId, e);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage("Unexpected error: " + e.getMessage());
        } finally {
            transaction.setEndTime(LocalDateTime.now());
            transaction.setTransformedData(null);
        }

        return mapToDto(transaction);
    }

    public EtlTransactionDTO getTransactionStatus(String transactionId) {
        EtlTransactionDTO transaction = transactions.get(transactionId);
        if (transaction == null) {
            throw new ResourceNotFoundException(transactionId);
        }
        return mapToDto(transaction);
    }

    public List<EtlTransactionDTO> getAllTransactions() {
        return transactions.values().stream().
                map(this::mapToDto)
                .toList();
    }

    private EtlTransactionDTO mapToDto(EtlTransactionDTO internalState) {
        EtlTransactionDTO dto = new EtlTransactionDTO();
        dto.setTransactionId(internalState.getTransactionId());
        dto.setStatus(internalState.getStatus());
        dto.setStartTime(internalState.getStartTime());
        dto.setEndTime(internalState.getEndTime());
        dto.setSourceDetails(internalState.getSourceDetails());
        dto.setExtractedRecordsCount(internalState.getExtractedRecordsCount());
        dto.setTransformedRecordsCount(internalState.getTransformedRecordsCount());
        dto.setLoadedRecordsCount(internalState.getLoadedRecordsCount());
        dto.setErrorMessage(internalState.getErrorMessage());
        return dto;
    }
}
