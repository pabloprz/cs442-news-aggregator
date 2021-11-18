package com.iit.pab.newsaggregator.dto;

public class CountryDTO implements Comparable<CountryDTO> {

    String code;
    String name;

    public CountryDTO(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int compareTo(CountryDTO countryDTO) {
        if (countryDTO != null && this.getName() != null && countryDTO.getName() != null) {
            return this.getName().compareTo(countryDTO.getName());
        }
        return 0;
    }
}
