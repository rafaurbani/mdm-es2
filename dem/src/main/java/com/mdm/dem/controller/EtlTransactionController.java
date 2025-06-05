package com.mdm.dem.controller;

import com.mdm.dem.dto.MasterDataDTO;
import com.mdm.dem.dto.TransactionDTO;
import com.mdm.dem.service.EtlTransactionService;
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
    public ResponseEntity<List<MasterDataDTO>> startEtl() {
        String source = "https://restcountries.com/v3.1/all";
        List<MasterDataDTO> transaction = etlTransactionService.initiateEtl(source);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(transaction);
    }

    @GetMapping("/{transactionId}")
    public ResponseEntity<TransactionDTO> getTransactionStatus(@PathVariable String transactionId) {
        TransactionDTO transaction = etlTransactionService.getTransactionStatus(transactionId);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping
    public ResponseEntity<Iterable<TransactionDTO>> getAllTransactions() {
        Iterable<TransactionDTO> transactions = etlTransactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

}
