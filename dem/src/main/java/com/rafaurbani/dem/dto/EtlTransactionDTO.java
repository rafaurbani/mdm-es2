package com.rafaurbani.dem.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Data;

@Data
public class EtlTransactionDTO {
    private String transactionId;
    private String status;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String sourceDetails;
    private int extractedRecordsCount;
    private int transformedRecordsCount;
    private int loadedRecordsCount;
    private String errorMessage;
    private List<MasterDataCountryDTO> transformedData;

    public EtlTransactionDTO() {
        this.transactionId = UUID.randomUUID().toString().substring(0, 6);
        this.startTime = LocalDateTime.now();
        this.status = "STARTED";
        this.transformedData = new ArrayList<>();
    }
}
