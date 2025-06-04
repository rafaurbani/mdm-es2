package com.rafaurbani.dem.controller;

import com.rafaurbani.dem.dto.EtlTransactionDTO;
import com.rafaurbani.dem.service.EtlTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dem")
public class EtlTransactionController {
    private final EtlTransactionService etlTransactionService;

    @Autowired
    public EtlTransactionController(EtlTransactionService etlTransactionService) {
        this.etlTransactionService = etlTransactionService;
    }

    @PostMapping
    public ResponseEntity<EtlTransactionDTO> startEtl(String source) {
        EtlTransactionDTO transaction = etlTransactionService.initiateEtl(source);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transaction);
    }

    @PostMapping("/{transactionId}/load")
    public ResponseEntity<EtlTransactionDTO> loadEtl(@PathVariable String transactionId) {
        EtlTransactionDTO transaction = etlTransactionService.loadToMdm(transactionId);
        if (transaction.getStatus().startsWith("FAILED")) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(transaction);
        }

        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<EtlTransactionDTO> getTransactionStatus(@PathVariable String transactionId) {
        EtlTransactionDTO transaction = etlTransactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<List<EtlTransactionDTO>> getAllTransactions() {
        List<EtlTransactionDTO> transactions = etlTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

}
