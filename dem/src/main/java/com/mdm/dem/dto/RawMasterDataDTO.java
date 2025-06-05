package com.mdm.dem.dto;

import lombok.Data;

import java.util.Map;

@Data
public class RawMasterDataDTO {
    private NameDTO name;
    private String[] tld;
    private String cca2;
    private String ccn3;
    private String cioc;
    private boolean independent;
    private String status;
    private boolean unMember;
    private Map<String, CurrencyDetailsDTO> currencies;
    private IddDTO idd;
    private String[] capital;
    private String[] altSpellings;
    private String region;
    private String subregion;
    private Map<String, String> languages;
    private int[] latlng;
    private boolean landlocked;
    private String[] borders;
    private int area;
    private Map<String, DemonymsDTO> demonyms;
    private String cca3;
    private Map<String, TranslationsDTO> translations;
    private String flag;
    private MapsDTO maps;
    private int population;
    private Map<String, Double> gini;
    private String fifa;
    private CarDTO car;
    private String[] timezones;
    private String[] continents;
    private FlagsDTO flags;
    private CoatOfArmsDTO coatOfArms;
    private String startOfWeek;
    private Map<String, Integer[]> capitalInfo;
    private PostalCodeDTO postalCode;

    @Data
    public static class NameDTO {
        private String common;
        private String official;
        private Map<String, NativeNameDTO> nativeName;
    }

    @Data
    public static class NativeNameDTO {
        private String official;
        private String common;
    }

    @Data
    public static class CurrencyDetailsDTO {
        private String symbol;
        private String name;
    }

    @Data
    public static class IddDTO {
        private String root;
        private String[] suffixes;
    }

    @Data
    public static class DemonymsDTO {
        private String f;
        private String m;
    }

    @Data
    public static class TranslationsDTO {
        private String official;
        private String common;
    }

    @Data
    public static class MapsDTO {
        private String googleMaps;
        private String openStreetMaps;
    }

    @Data
    public static class CarDTO {
        private String[] signs;
        private String side;
    }

    @Data
    public static class FlagsDTO {
        private String png;
        private String svg;
        private String alt;
    }

    @Data
    public static class CoatOfArmsDTO {
        private String png;
        private String svg;
    }

    @Data
    public static class PostalCodeDTO {
        private String format;
        private String regex;
    }
}