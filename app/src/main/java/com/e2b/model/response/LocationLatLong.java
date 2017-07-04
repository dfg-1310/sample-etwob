package com.e2b.model.response;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LocationLatLong {
    private List<Object> html_attributions = null;
    private Result result;
    private String status;
    private Map<String, Object> additional_properties = new HashMap<String, Object>();

    public List<Object> getHtmlAttributions() {
        return html_attributions;
    }

    public void setHtmlAttributions(List<Object> htmlAttributions) {
        this.html_attributions = htmlAttributions;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additional_properties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additional_properties.put(name, value);
    }

}
