package model;

public class House extends Entity {
    private int numberOfFloors;
    private boolean hasGarage;

    public House(String address, double area, int numberOfFloors, boolean hasGarage) {
        super(address, area);
        this.numberOfFloors = numberOfFloors;
        this.hasGarage = hasGarage;
    }

    public int getNumberOfFloors() {
        return numberOfFloors;
    }

    public void setNumberOfFloors(int numberOfFloors) throws IllegalArgumentException {
        if (numberOfFloors <= 0) {
            throw new IllegalArgumentException("Flat's number should be more than zero");
        }
        this.numberOfFloors = numberOfFloors;
    }

    public boolean hasGarage() {
        return hasGarage;
    }

    public void setHasGarage(boolean hasGarage) {
        this.hasGarage = hasGarage;
    }
}