package repository.file;

import model.Office;
import repository.AbstractFileRepository;

public class OfficeRepository extends AbstractFileRepository<Office> {
    private final String delimiter = "\0";

    public OfficeRepository(String filePath) throws Exception {
        super(filePath);
    }

    @Override
    protected Office deserialize(String content) throws Exception {
        String[] parts = content.split(delimiter);
        if (parts.length != 3) {
            throw new Exception("Incorrect data format for Office");
        }
        String address = parts[0];
        double area = Double.parseDouble(parts[1]);
        String company = parts[2];
        return new Office(address, area, company);
    }

    @Override
    protected String serialize(Office object) {
        return object.getAddress() + delimiter +
                object.getArea() + delimiter +
                object.getOwnerCompany();
    }
}
