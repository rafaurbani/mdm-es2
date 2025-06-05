package com.mdm.dem.service;

import com.mdm.dem.dto.MasterDataRawDTO;
import com.mdm.dem.dto.MasterDataTransformedDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransformationService {
    public List<MasterDataTransformedDTO> transformRawMasterData(List<MasterDataRawDTO> rawDataList) {
        return rawDataList.stream().map(rawData -> {
            MasterDataTransformedDTO masterDataTransformed = new MasterDataTransformedDTO();
            masterDataTransformed.setCode(rawData.getCioc());
            masterDataTransformed.setName(rawData.getName().getCommon());
            masterDataTransformed.setNumericCode(getNumericCodeFromRawData(rawData));
            masterDataTransformed.setCapitalCity(getCapitalFromRawData(rawData));
            masterDataTransformed.setPopulation(rawData.getPopulation());
            masterDataTransformed.setArea(rawData.getArea());
            return masterDataTransformed;
        }).toList();
    }

    public int getNumericCodeFromRawData(MasterDataRawDTO rawCountry) {
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

    public String getCapitalFromRawData(MasterDataRawDTO rawCountry) {
        if (rawCountry.getCapital() != null && rawCountry.getCapital().length > 0) {
            return rawCountry.getCapital()[0];
        } else {
            return "N/A";
        }
    }
}
