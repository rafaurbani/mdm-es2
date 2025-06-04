package com.rafaurbani.dem.client;

import com.rafaurbani.dem.dto.MasterDataCountryDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import java.util.List;

@FeignClient(name = "mdm-microservice", fallback = MdmServiceFallback.class)
public interface MdmServiceProxy {
    @PostMapping("/api/mdm/")
    void loadCountriesToMdm(@RequestBody List<MasterDataCountryDTO> countryDataList);
}
