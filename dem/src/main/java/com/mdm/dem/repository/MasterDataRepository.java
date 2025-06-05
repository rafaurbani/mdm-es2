package com.mdm.dem.repository;

import com.mdm.dem.dto.MasterDataDTO;
import org.springframework.data.repository.CrudRepository;

public interface MasterDataRepository extends CrudRepository<MasterDataDTO, String> {
}
