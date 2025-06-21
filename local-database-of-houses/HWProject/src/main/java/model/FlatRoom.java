package model;

public class FlatRoom extends Entity {
    private int floor;
    private int roomNumber;


    public FlatRoom(String address, double area, int floor, int roomNumber) {
        super(address, area);
        this.floor = floor;
        this.roomNumber = roomNumber;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
