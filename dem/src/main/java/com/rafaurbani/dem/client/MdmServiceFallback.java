package com.rafaurbani.dem.client;

import com.rafaurbani.dem.dto.MasterDataCountryDTO;
import com.rafaurbani.dem.exception.MdmServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class MdmServiceFallback implements MdmServiceProxy {

    private static final Logger logger = LoggerFactory.getLogger(MdmServiceFallback.class);

    public void loadCountriesToMdm(List<MasterDataCountryDTO> countryDataList) {
        String errorMessage = String.format(
                "Error calling MDM Service. Unable to load %d country records.",
                countryDataList.size()
        );

        logger.error(errorMessage);
        throw new MdmServiceUnavailableException(errorMessage);
    }
}