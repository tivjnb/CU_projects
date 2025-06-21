package repository.file;

import model.House;
import repository.AbstractFileRepository;

public class HouseRepository extends AbstractFileRepository<House> {
    private final String delimiter = "\0";

    public HouseRepository(String filePath) throws Exception {
        super(filePath);
    }

    @Override
    protected House deserialize(String content) throws Exception {
        String[] parts = content.split(delimiter);
        if (parts.length != 4) {
            throw new Exception("Incorrect data format for House");
        }
        String address = parts[0];
        double area = Double.parseDouble(parts[1]);
        int floors = Integer.parseInt(parts[2]);
        boolean hasGarage = Boolean.parseBoolean(parts[3]);
        return new House(address, area, floors, hasGarage);
    }

    @Override
    protected String serialize(House object) {
        return object.getAddress() + delimiter +
                object.getArea() + delimiter +
                object.getNumberOfFloors() + delimiter +
                object.hasGarage();
    }
}