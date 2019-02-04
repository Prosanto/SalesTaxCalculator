package com.sales.taxcalculator.model;

import org.json.JSONObject;

public class Periods {
    private String effective_from="";
    private JSONObject rates=null;

    public String getEffective_from() {
        return effective_from;
    }

    public void setEffective_from(String effective_from) {
        this.effective_from = effective_from;
    }

    public JSONObject getRates() {
        return rates;
    }

    public void setRates(JSONObject rates) {
        this.rates = rates;
    }
}
