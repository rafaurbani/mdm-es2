package com.mdm.dem.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

import java.time.LocalDateTime;

@Data @Entity
public class MasterDataDTO {
    @Id
    private String countryID;
    private String countryName;
    private int numericCode;
    private String capitalCity;
    private int population;
    private float area;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public MasterDataDTO() {
    }
}
