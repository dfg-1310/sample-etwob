package com.e2b.model;

import java.util.HashMap;
import java.util.Map;

public class Geometry {

    private Location location;


    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public Map<String, Object> getAdditional_properties() {
        return additional_properties;
    }

    public void setAdditional_properties(Map<String, Object> additional_properties) {
        this.additional_properties = additional_properties;
    }

    private Map<String, Object> additional_properties = new HashMap<String, Object>();



}