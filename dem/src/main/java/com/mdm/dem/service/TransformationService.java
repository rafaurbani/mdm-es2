package com.mdm.dem.service;

import com.mdm.dem.dto.RawMasterDataDTO;
import com.mdm.dem.dto.MasterDataDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransformationService {
    private static final Logger logger = LoggerFactory.getLogger(TransformationService.class);

    public List<MasterDataDTO> transformRawMasterData(List<RawMasterDataDTO> rawDataList) {
        return rawDataList.stream().map(rawData -> {
            MasterDataDTO masterDataTransformed = new MasterDataDTO();
            masterDataTransformed.setCountryID(rawData.getCca2());
            masterDataTransformed.setCountryName(rawData.getName().getCommon());
            masterDataTransformed.setNumericCode(getNumericCodeFromRawData(rawData));
            masterDataTransformed.setCapitalCity(getCapitalFromRawData(rawData));
            masterDataTransformed.setPopulation(rawData.getPopulation());
            masterDataTransformed.setArea(rawData.getArea());
            masterDataTransformed.setCreatedAt(LocalDateTime.now());
            masterDataTransformed.setUpdatedAt(LocalDateTime.now());
            return masterDataTransformed;
        }).toList();
    }

    public int getNumericCodeFromRawData(RawMasterDataDTO rawCountry) {
        if (rawCountry.getCcn3() != null) {
            return Integer.parseInt(rawCountry.getCcn3());
        } else if (rawCountry.getIdd() != null && rawCountry.getIdd().getRoot() != null) {
            String numericCode = rawCountry.getIdd().getRoot();
            if (numericCode.startsWith("+")) {
                numericCode = numericCode.substring(1);
            }
            return Integer.parseInt(numericCode);
        } else {
            throw new IllegalArgumentException("Numeric code not available in raw data");
        }
    }

    public String getCapitalFromRawData(RawMasterDataDTO rawCountry) {
        if (rawCountry.getCapital() != null && rawCountry.getCapital().length > 0) {
            return rawCountry.getCapital()[0];
        } else {
            return "N/A";
        }
    }
}
