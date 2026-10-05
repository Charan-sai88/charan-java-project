package com.example.store;

import java.util.LinkedHashMap;
import java.util.Map;

public class CreateBillForm {

    private Map<Long, Integer> quantities = new LinkedHashMap<>();

    public Map<Long, Integer> getQuantities() {
        return quantities;
    }

    public void setQuantities(Map<Long, Integer> quantities) {
        this.quantities = quantities == null ? new LinkedHashMap<>() : quantities;
    }
}
