package com.mdm.dem.repository;

import com.mdm.dem.dto.EtlTransactionDTO;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<EtlTransactionDTO, String> {
}
