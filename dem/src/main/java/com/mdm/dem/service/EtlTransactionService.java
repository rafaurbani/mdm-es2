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
    private final LoadingService loadingService;

    public EtlTransactionService(TransactionRepository transactionRepository, ExtractionService extractionService,
                                 TransformationService transformationService, LoadingService loadingService) {
        this.transactionRepository = transactionRepository;
        this.extractionService = extractionService;
        this.transformationService = transformationService;
        this.loadingService = loadingService;
    }

    public List<MasterDataDTO> initiateEtl(String source) {
        TransactionDTO transaction = new TransactionDTO();
        List<MasterDataDTO> transformedData = null;
        String transactionId = UUID.randomUUID().toString().substring(0, 6);

        transaction.setStartTime(LocalDateTime.now());
        transaction.setSourceDetails(source);
        transaction.setTransactionId(transactionId);

        logger.info("Starting ETL transaction [{}] with source: {}", transactionId, source);
        try {
            List<RawMasterDataDTO> rawData = extraction(transaction);
            transformedData = transformation(transaction, rawData);
            loading(transaction, transformedData);
        } catch (Exception ex) {
            logger.error("Transaction [{}]: ETL process failed with error: {}", transactionId, ex.getMessage(), ex);
            transaction.setStatus("FAILED");
            transaction.setErrorMessage(ex.getMessage());
            transactionRepository.save(transaction);
            return Collections.emptyList();
        }

        transaction.setEndTime(LocalDateTime.now());
        transactionRepository.save(transaction);
        return transformedData;
    }

    public List<RawMasterDataDTO> extraction(TransactionDTO transaction) throws Exception {
        logger.info("Transaction [{}]: Starting extraction phase.", transaction.getTransactionId());
        logger.info(transaction.getSourceDetails());
        List<RawMasterDataDTO> rawData = extractionService.fetchRawData(transaction.getSourceDetails());
        transaction.setExtractedRecordsCount(rawData.size());
        logger.info("Transaction [{}]: Extraction completed with {} records.", transaction.getTransactionId(), rawData.size());

        if (rawData.isEmpty() && transaction.getExtractedRecordsCount() == 0) {
            logger.warn("Transaction [{}]: No data extracted from source. Aborting ETL process.", transaction.getTransactionId());
            throw new Exception("No data extracted from source");
        }

        transaction.setStatus("EXTRACTED");
        transaction.setEndTime(LocalDateTime.now());
        transactionRepository.save(transaction);

        return rawData;
    }

    public List<MasterDataDTO> transformation(TransactionDTO transaction, List<RawMasterDataDTO> rawData) {

        transaction.setStatus("TRANSFORMING");
        logger.info("Transaction [{}]: Starting transformation phase.", transaction.getTransactionId());
        List<MasterDataDTO> transformedData = transformationService.transformRawMasterData(rawData);
        transaction.setTransformedRecordsCount(transformedData.size());
        logger.info("Transaction [{}]: Transformation completed with {} records.", transaction.getTransactionId(), transformedData.size());
        transaction.setStatus("TRANSFORMED");

        return transformedData;
    }

    public void loading(TransactionDTO transaction, List<MasterDataDTO> transformedData) {

        transaction.setStatus("LOADING");
        logger.info("Transaction [{}]: Starting loading of {} entries.", transaction.getTransactionId(), transformedData.size());
        loadingService.loadDataToDB(transformedData);
        transaction.setLoadedRecordsCount(transformedData.size());
        transaction.setStatus("DONE");
        logger.info("Transaction [{}]: Loading completed successfully.", transaction.getTransactionId());

        transaction.setEndTime(LocalDateTime.now());
        transactionRepository.save(transaction);
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
