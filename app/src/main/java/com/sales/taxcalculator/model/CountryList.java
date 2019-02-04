package com.sales.taxcalculator.model;

import java.util.ArrayList;

public class CountryList {
    private String name="";
    private String code="";
    private String country_code="";
    private ArrayList<Periods>arrayList= new ArrayList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCountry_code() {
        return country_code;
    }

    public void setCountry_code(String country_code) {
        this.country_code = country_code;
    }

    public ArrayList<Periods> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<Periods> arrayList) {
        this.arrayList = arrayList;
    }
}
