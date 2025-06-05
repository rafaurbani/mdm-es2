package com.mdm.dem.dto;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class MasterDataDTO {
    @Id
    private String code;
    private String name;
    private int numericCode;
    private String capitalCity;
    private int population;
    private float area;
}
