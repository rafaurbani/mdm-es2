package com.mdm.dem.dto;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class TransactionDTO {
    @Id
    private String transactionId;

    private String status;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private String sourceDetails;

    private int extractedRecordsCount;

    private int transformedRecordsCount;

    private int loadedRecordsCount;

    @Column(columnDefinition = "LONGTEXT")
    private String errorMessage;
}
