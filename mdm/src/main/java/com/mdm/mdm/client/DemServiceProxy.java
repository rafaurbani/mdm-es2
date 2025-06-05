package com.mdm.mdm.client;

import com.mdm.mdm.dto.MasterDataDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "dem-microservice", fallback = DemServiceFallback.class)
public interface DemServiceProxy {
    @PostMapping("/api/dem/")
    void loadMasterData(@RequestBody List<MasterDataDTO> masterDataList);
}
