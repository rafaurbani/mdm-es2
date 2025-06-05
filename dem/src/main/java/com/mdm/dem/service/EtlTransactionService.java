package com.mdm.dem.service;

import com.mdm.dem.dto.TransactionDTO;
import com.mdm.dem.dto.RawMasterDataDTO;
import com.mdm.dem.dto.MasterDataDTO;
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

    public TransactionDTO initiateEtl(String source) {
        TransactionDTO transaction = new TransactionDTO();
        String transactionId = UUID.randomUUID().toString().substring(0, 6);

        transaction.setStartTime(LocalDateTime.now());
        transaction.setSourceDetails(source);
        transaction.setTransactionId(transactionId);

        logger.info("Starting ETL transaction [{}] with source: {}", transactionId, source);

        try {
            // extraction
            logger.info("Transaction [{}]: Starting extraction phase.", transactionId);
            List<RawMasterDataDTO> rawData = extractionService.fetchRawData(source);
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
            List<MasterDataDTO> transformedData = transformationService.transformRawMasterData(rawData);
            transaction.setTransformedRecordsCount(transformedData.size());
            logger.info("Transaction [{}]: Transformation completed with {} records.", transactionId, transformedData.size());

            // loading
            transaction.setStatus("LOADING");
            logger.info("Transaction [{}]: Starting loading of {} entries.", transactionId, transformedData.size());
            loadingService.loadDataToBD(transaction);
            transaction.setLoadedRecordsCount(transaction.getTransformedData().size());
            transaction.setStatus("DONE");
            logger.info("Transaction [{}]: Loading completed successfully.", transactionId);

        } catch (Exception e) {
            logger.error("Transaction [{}]: Unexpected error during ETL process: {}", transactionId, e.getMessage(), e);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage(e.getMessage());
        }

        transaction.setEndTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transaction;
    }

    public TransactionDTO getTransactionStatus(String transactionId) {
        TransactionDTO transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction == null) {
            throw new NotFoundException(transactionId);
        }

        return transaction;
    }

    public Iterable<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll();
    }
}
