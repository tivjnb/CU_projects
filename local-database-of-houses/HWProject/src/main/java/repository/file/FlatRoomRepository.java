package repository.file;

import model.FlatRoom;
import repository.AbstractFileRepository;

public class FlatRoomRepository extends AbstractFileRepository<FlatRoom> {
    private final String delimiter = "\0";

    public FlatRoomRepository(String filePath) throws Exception {
        super(filePath);
    }

    @Override
    protected FlatRoom deserialize(String content) throws Exception {
        String[] parts = content.split(delimiter);
        if (parts.length != 4) {
            throw new Exception("Incorrect data format for FlatRoom");
        }
        String address = parts[0];
        double area = Double.parseDouble(parts[1]);
        int floor = Integer.parseInt(parts[2]);
        int apartmentNumber = Integer.parseInt(parts[3]);
        return new FlatRoom(address, area, floor, apartmentNumber);
    }

    @Override
    protected String serialize(FlatRoom object) {
        return object.getAddress() + delimiter +
                object.getArea() + delimiter +
                object.getFloor() + delimiter +
                object.getRoomNumber();
    }
}