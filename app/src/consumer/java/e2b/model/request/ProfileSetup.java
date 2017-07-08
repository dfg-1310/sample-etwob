package e2b.model.request;

import com.e2b.model.request.Coordinate;

public class ProfileSetup extends BaseRequest{

    private String name;
    private String address;
    private Coordinate coordinates;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Coordinate getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinate coordinates) {
        this.coordinates = coordinates;
    }



}
