package com.mdm.mdm.client;

import com.mdm.mdm.dto.MasterDataDTO;
import jakarta.ws.rs.ServiceUnavailableException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class DemServiceFallback implements DemServiceProxy {

    private static final Logger logger = LoggerFactory.getLogger(DemServiceFallback.class);

    public List<MasterDataDTO> startEtl(String source) {
        String errorMessage = "Error calling DEM Service. Unable to load records.";

        logger.error(errorMessage);
        throw new ServiceUnavailableException(errorMessage);
    }
}
