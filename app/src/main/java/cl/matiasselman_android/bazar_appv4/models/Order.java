package cl.matiasselman_android.bazar_appv4.models;

import com.google.firebase.firestore.GeoPoint;

public class Order {
    private String idOder, clientName, description, date, placeName, address;
    private GeoPoint geoPoint;

    public Order(String idOder, String clientName, String description, String date, String placeName, String address, GeoPoint geoPoint) {
        this.idOder = idOder;
        this.clientName = clientName;
        this.description = description;
        this.date = date;
        this.placeName = placeName;
        this.address = address;
        this.geoPoint = geoPoint;
    }

    public String getIdOder() {
        return idOder;
    }

    public void setIdOder(String idOder) {
        this.idOder = idOder;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlaceName() {
        return placeName;
    }

    public void setPlaceName(String placeName) {
        this.placeName = placeName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public GeoPoint getGeoPoint() {
        return geoPoint;
    }

    public void setGeoPoint(GeoPoint geoPoint) {
        this.geoPoint = geoPoint;
    }
}
