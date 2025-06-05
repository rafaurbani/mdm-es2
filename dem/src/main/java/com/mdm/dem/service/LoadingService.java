package com.mdm.dem.service;

import com.mdm.dem.dto.TransactionDTO;
import com.mdm.dem.repository.MasterDataRepository;
import org.springframework.stereotype.Service;

@Service
public class LoadingService {

    private final MasterDataRepository masterDataRepository;

    public LoadingService(MasterDataRepository masterDataRepository) {
        this.masterDataRepository = masterDataRepository;
    }

    public void loadDataToDB(TransactionDTO transaction) {

    }
}
