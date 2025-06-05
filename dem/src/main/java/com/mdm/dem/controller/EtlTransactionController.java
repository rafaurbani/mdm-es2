package com.mdm.dem.controller;

import com.mdm.dem.dto.EtlTransactionDTO;
import com.mdm.dem.service.EtlTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dem")
public class EtlTransactionController {
    private final EtlTransactionService etlTransactionService;

    @Autowired
    public EtlTransactionController(EtlTransactionService etlTransactionService) {
        this.etlTransactionService = etlTransactionService;
    }

    @PostMapping
    public ResponseEntity<EtlTransactionDTO> startEtl() {
        String source = "https://restcountries.com/v3.1/all";
        EtlTransactionDTO transaction = etlTransactionService.initiateEtl(source);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<EtlTransactionDTO> getTransactionStatus(@PathVariable String transactionId) {
        EtlTransactionDTO transaction = etlTransactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Iterable<EtlTransactionDTO>> getAllTransactions() {
        Iterable<EtlTransactionDTO> transactions = etlTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

}
