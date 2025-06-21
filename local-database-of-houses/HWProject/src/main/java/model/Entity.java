package model;

public abstract class Entity {
    private String address;
    private double  area;

    public Entity (String address, double area) {
        this.address = address;
        this.area = area;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) throws IllegalArgumentException{
        if (area <= 0) {
            throw  new IllegalArgumentException("Area cannot be negative");
        }
        this.area = area;
    }

    public String getAddress(){
        return address;
    }

    public void setAddress(String address) throws IllegalArgumentException{
        if (address.isBlank() ) {
            throw  new IllegalArgumentException("Address cannot be blank");
        }
        this.address = address;
    }
}
