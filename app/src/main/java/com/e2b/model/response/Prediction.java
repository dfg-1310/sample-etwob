package com.e2b.model.response;

public class Prediction {
    private String description;
    private String id;
    private String place_id;
    private String reference;



    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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



}