package com.rafaurbani.dem.dto;

import lombok.Data;

@Data
public class MasterDataCountryDTO {
    private String code;
    private String name;
    private int numericCode;
    private String capitalCity;
    private int population;
    private float area;
}
