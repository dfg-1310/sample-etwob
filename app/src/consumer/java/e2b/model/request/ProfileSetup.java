package e2b.model.request;

import com.e2b.model.request.Coordinate;

public class ProfileSetup extends BaseRequest{

    private String name;
    private String address;
    private Coordinate coordinate;

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

    public Coordinate getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(Coordinate coordinate) {
        this.coordinate = coordinate;
    }



}
