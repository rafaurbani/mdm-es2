package com.mdm.mdm.controller;

import com.mdm.mdm.client.DemServiceProxy;
import com.mdm.mdm.dto.MasterDataDTO;
import com.mdm.mdm.service.MasterDataService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/mdm")
public class MasterDataController {
    private final MasterDataService masterDataService;
    private final DemServiceProxy demServiceProxy;

    public MasterDataController(MasterDataService masterDataService, DemServiceProxy demServiceProxy) {
        this.masterDataService = masterDataService;
        this.demServiceProxy = demServiceProxy;
    }

    @PostMapping("/source")
    public ResponseEntity<List<MasterDataDTO>> createMasterDataFromDem(@RequestBody String source) {
        List<MasterDataDTO> masterDataList = demServiceProxy.startEtl(source);
        if (masterDataList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok().body(masterDataList);
        }
    }

    @PostMapping("")
    public ResponseEntity<MasterDataDTO> create(@Valid @RequestBody MasterDataDTO data) {
        Optional<MasterDataDTO> masterData = masterDataService.findOne(data.getCountryID());

        // check if the master data already exists
        if (masterData.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(masterData.get());
        } else {
            return ResponseEntity.status(HttpStatus.CREATED).body(masterDataService.create(data));
        }
    }

    @GetMapping("")
    public ResponseEntity<Iterable<MasterDataDTO>> findAll() {
        Iterable<MasterDataDTO> listMasterData = masterDataService.findAll();

        if (!listMasterData.iterator().hasNext()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(listMasterData);
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<MasterDataDTO> findOne(@PathVariable String code) {
        Optional<MasterDataDTO> masterData = masterDataService.findOne(code);

        if (masterData.isPresent()) {
            return ResponseEntity.ok(masterData.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<MasterDataDTO> update(@PathVariable String code, @Valid @RequestBody MasterDataDTO data) {
        Optional<MasterDataDTO> masterData = masterDataService.update(code, data);

        if (masterData.isPresent()) {
            return ResponseEntity.ok(masterData.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<Boolean> delete(@PathVariable String code) {
        boolean deleted = masterDataService.delete(code);

        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}