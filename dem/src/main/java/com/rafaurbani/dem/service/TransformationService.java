package com.rafaurbani.dem.service;

import com.rafaurbani.dem.dto.CountryRawDTO;
import com.rafaurbani.dem.dto.MasterDataCountryDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransformationService {
    public List<MasterDataCountryDTO> transformCountryData(List<CountryRawDTO> countryRawDataList) {
        return countryRawDataList.stream().map(rawCountry -> {
            MasterDataCountryDTO masterData = new MasterDataCountryDTO();
            masterData.setCode(rawCountry.getCioc());
            masterData.setName(rawCountry.getName().getCommon());
            masterData.setNumericCode(getNumericCodeFromRawData(rawCountry));
            masterData.setCapitalCity(getCapitalFromRawData(rawCountry));
            masterData.setPopulation(rawCountry.getPopulation());
            masterData.setArea(rawCountry.getArea());
            return masterData;
        }).toList();
    }

    public int getNumericCodeFromRawData(CountryRawDTO rawCountry) {
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

    public String getCapitalFromRawData(CountryRawDTO rawCountry) {
        if (rawCountry.getCapital() != null && rawCountry.getCapital().length > 0) {
            return rawCountry.getCapital()[0];
        } else {
            return "N/A";
        }
    }
}
