package com.mdm.dem.service;

import com.mdm.dem.dto.MasterDataDTO;
import com.mdm.dem.repository.MasterDataRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LoadingService {

    private final MasterDataRepository masterDataRepository;

    public LoadingService(MasterDataRepository masterDataRepository) {
        this.masterDataRepository = masterDataRepository;
    }

    public void loadDataToDB(List<MasterDataDTO> listMasterData) {
        if (listMasterData == null || listMasterData.isEmpty()) {
            throw new IllegalArgumentException("No data to load into the database");
        }
        masterDataRepository.saveAll(listMasterData);
    }
}
