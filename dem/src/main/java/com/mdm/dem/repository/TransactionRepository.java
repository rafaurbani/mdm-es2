package com.mdm.dem.repository;

import com.mdm.dem.dto.TransactionDTO;
import org.springframework.data.repository.CrudRepository;

public interface TransactionRepository extends CrudRepository<TransactionDTO, String> {
}
