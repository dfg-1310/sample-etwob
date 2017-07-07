package com.e2b.model.response;

import com.e2b.model.Geometry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Result {


    private String adr_address;
    private String formatted_address;
    private Geometry geometry;
    private String icon;
    private String id;
    private String name;
    private String place_id;
    private String reference;
    private String scope;
    private List<String> types = null;
    private String url;
    private Integer utc_offset;
    private String vicinity;
    private Map<String, Object> additional_properties = new HashMap<String, Object>();


    public String getAdrAddress() {
        return adr_address;
    }

    public void setAdrAddress(String adrAddress) {
        this.adr_address = adrAddress;
    }

    public String getFormattedAddress() {
        return formatted_address;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formatted_address = formattedAddress;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlaceId() {
        return place_id;
    }

    public void setPlaceId(String placeId) {
        this.place_id = placeId;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getUtcOffset() {
        return utc_offset;
    }

    public void setUtcOffset(Integer utcOffset) {
        this.utc_offset = utcOffset;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additional_properties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additional_properties.put(name, value);
    }

}