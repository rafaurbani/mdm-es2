package com.mdm.mdm.repository;

import com.mdm.mdm.dto.MasterDataDTO;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MasterDataRepository extends CrudRepository<MasterDataDTO, String> {
}
