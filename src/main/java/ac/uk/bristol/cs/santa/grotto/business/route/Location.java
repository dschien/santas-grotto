package ac.uk.bristol.cs.santa.grotto.business.route;

public class Location {
    private double longitude;
    private double latitude;
    private String name;

    public Location(String name, Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name + "(" + latitude + "," + longitude + ")";
    }
}
