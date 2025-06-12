package com.mdm.mdm.service;

import com.mdm.mdm.repository.MasterDataRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import com.mdm.mdm.dto.MasterDataDTO;

@Service
public class MasterDataService {

    private final MasterDataRepository masterDataRepository;

    public MasterDataService(MasterDataRepository masterDataRepository) {
        this.masterDataRepository = masterDataRepository;
    }

    public MasterDataDTO create(MasterDataDTO data) {
        data.setCreatedAt(LocalDateTime.now());
        data.setUpdatedAt(LocalDateTime.now());
        return masterDataRepository.save(data);
    }

    public Iterable<MasterDataDTO> findAll() {
        return masterDataRepository.findAll();
    }

    public Optional<MasterDataDTO> findOne(String code) {
        return masterDataRepository.findById(code);
    }

    public Optional<MasterDataDTO> update(String code, MasterDataDTO data) {
        if (!masterDataRepository.existsById(code)) {
            return Optional.empty();
        }
        data.setCountryID(code);
        data.setCreatedAt(masterDataRepository.findById(code).get().getCreatedAt());
        data.setUpdatedAt(LocalDateTime.now());
        return Optional.of(masterDataRepository.save(data));
    }

    public boolean delete(String code) {
        if (masterDataRepository.existsById(code)) {
            masterDataRepository.deleteById(code);
            return true;
        }
        return false;
    }
}
