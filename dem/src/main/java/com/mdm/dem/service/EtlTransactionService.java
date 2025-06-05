package com.mdm.dem.service;

import com.mdm.dem.dto.EtlTransactionDTO;
import com.mdm.dem.dto.MasterDataRawDTO;
import com.mdm.dem.dto.MasterDataTransformedDTO;
import com.mdm.dem.repository.TransactionRepository;
import jakarta.ws.rs.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EtlTransactionService {
    private static final Logger logger = LoggerFactory.getLogger(EtlTransactionService.class);
    private final TransactionRepository transactionRepository;

    private final ExtractionService extractionService;
    private final TransformationService transformationService;

    public EtlTransactionService(TransactionRepository transactionRepository, ExtractionService extractionService,
                                 TransformationService transformationService) {
        this.transactionRepository = transactionRepository;
        this.extractionService = extractionService;
        this.transformationService = transformationService;
    }

    public EtlTransactionDTO initiateEtl(String source) {
        EtlTransactionDTO transaction = new EtlTransactionDTO();
        String transactionId = UUID.randomUUID().toString().substring(0, 6);

        transaction.setStartTime(LocalDateTime.now());
        transaction.setSourceDetails(source);
        transaction.setTransactionId(transactionId);

        logger.info("Starting ETL transaction [{}] with source: {}", transactionId, source);

        try {
            // extraction
            logger.info("Transaction [{}]: Starting extraction phase.", transactionId);
            List<MasterDataRawDTO> rawData = extractionService.fetchRawData(source);
            transaction.setExtractedRecordsCount(rawData.size());
            logger.info("Transaction [{}]: Extraction completed with {} records.", transactionId, rawData.size());

            if (rawData.isEmpty() && transaction.getExtractedRecordsCount() == 0) {
                logger.warn("Transaction [{}]: No data extracted from source. Aborting ETL process.", transactionId);
                transaction.setStatus("FAILED");
                throw new Exception("No data extracted from source");
            }

            // transformation
            transaction.setStatus("TRANSFORMING");
            logger.info("Transaction [{}]: Starting transformation phase.", transactionId);
            List<MasterDataTransformedDTO> transformedData = transformationService.transformRawMasterData(rawData);
            transaction.setTransformedRecordsCount(transformedData.size());
            logger.info("Transaction [{}]: Transformation completed with {} records.", transactionId, transformedData.size());
            transaction.setStatus("DONE");
        } catch (Exception e) {
            logger.error("Transaction [{}]: Unexpected error during ETL process: {}", transactionId, e.getMessage(), e);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage(e.getMessage());
        }

        transaction.setEndTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;
    }

    public EtlTransactionDTO getTransactionStatus(String transactionId) {
        EtlTransactionDTO transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction == null) {
            throw new NotFoundException(transactionId);
        }

        return transaction;
    }

    public Iterable<EtlTransactionDTO> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
